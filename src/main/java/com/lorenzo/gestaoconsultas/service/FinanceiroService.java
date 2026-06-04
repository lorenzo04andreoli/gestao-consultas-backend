package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoRequestDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoResponseDto;
import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.entity.FinanceiroLancamento;
import com.lorenzo.gestaoconsultas.enums.StatusConsulta;
import com.lorenzo.gestaoconsultas.repository.ConsultaRepository;
import com.lorenzo.gestaoconsultas.repository.FinanceiroLancamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinanceiroService {

    private final FinanceiroLancamentoRepository repository;
    private final ConsultaRepository consultaRepository;

    public FinanceiroService(FinanceiroLancamentoRepository repository,
                             ConsultaRepository consultaRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public FinanceiroLancamentoResponseDto criarCobranca(FinanceiroLancamentoRequestDto dto) {
        validarValor(dto.getValor());
        validarDescricao(dto.getDescricao());

        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new RuntimeException("Consulta nao encontrada"));

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new RuntimeException("Consulta cancelada nao pode gerar cobranca");
        }

        if (repository.existsByConsultaId(consulta.getId())) {
            throw new RuntimeException("Consulta ja possui cobranca cadastrada");
        }

        FinanceiroLancamento lancamento = new FinanceiroLancamento();
        lancamento.setConsulta(consulta);
        lancamento.setPaciente(consulta.getPaciente());
        lancamento.setDentista(consulta.getDentista());
        lancamento.setDescricao(dto.getDescricao().trim());
        lancamento.setValor(dto.getValor());
        lancamento.setDataVencimento(dto.getDataVencimento());

        return toResponseDto(repository.save(lancamento));
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor da cobranca deve ser maior que zero");
        }
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new RuntimeException("Descricao da cobranca e obrigatoria");
        }
    }

    private FinanceiroLancamentoResponseDto toResponseDto(FinanceiroLancamento lancamento) {
        return new FinanceiroLancamentoResponseDto(
                lancamento.getId(),
                lancamento.getConsulta().getId(),
                lancamento.getPaciente().getId(),
                lancamento.getPaciente().getNome(),
                lancamento.getDentista().getId(),
                lancamento.getDentista().getNome(),
                lancamento.getDescricao(),
                lancamento.getValor(),
                lancamento.getTipo().name(),
                lancamento.getStatus().name(),
                lancamento.getDataVencimento(),
                lancamento.getDataPagamento(),
                lancamento.getDataCriacao()
        );
    }
}
