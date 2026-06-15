package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoRequestDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoResponseDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroResumoResponseDto;
import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.entity.FinanceiroLancamento;
import com.lorenzo.gestaoconsultas.enums.StatusConsulta;
import com.lorenzo.gestaoconsultas.enums.StatusLancamentoFinanceiro;
import com.lorenzo.gestaoconsultas.repository.ConsultaRepository;
import com.lorenzo.gestaoconsultas.repository.FinanceiroLancamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinanceiroService {

    private final FinanceiroLancamentoRepository repository;
    private final ConsultaRepository consultaRepository;

    public FinanceiroService(FinanceiroLancamentoRepository repository,
                             ConsultaRepository consultaRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
    }

    public List<FinanceiroLancamentoResponseDto> listarLancamentos() {
        return repository.findAllByOrderByDataCriacaoDesc().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public FinanceiroLancamentoResponseDto buscarPorId(Long id) {
        return toResponseDto(buscarLancamentoPorId(id));
    }

    public FinanceiroResumoResponseDto resumo() {
        LocalDateTime inicioMes = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();
        LocalDateTime inicioProximoMes = inicioMes.plusMonths(1);

        BigDecimal recebidoMes = repository.somarPorStatusEDataPagamento(
                StatusLancamentoFinanceiro.PAGO,
                inicioMes,
                inicioProximoMes
        );

        BigDecimal aReceber = repository.findByStatus(StatusLancamentoFinanceiro.PENDENTE).stream()
                .map(FinanceiroLancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long pendentes = repository.countByStatus(StatusLancamentoFinanceiro.PENDENTE);
        Long pagas = repository.countByStatus(StatusLancamentoFinanceiro.PAGO);
        Long canceladas = repository.countByStatus(StatusLancamentoFinanceiro.CANCELADO);

        return new FinanceiroResumoResponseDto(
                recebidoMes,
                aReceber,
                pendentes,
                pagas,
                canceladas
        );
    }

    @Transactional
    public FinanceiroLancamentoResponseDto criarCobranca(FinanceiroLancamentoRequestDto dto) {
        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new RuntimeException("Consulta nao encontrada"));

        return criarCobrancaDaConsulta(
                consulta,
                dto.getValor(),
                dto.getDescricao(),
                dto.getDataVencimento()
        );
    }

    @Transactional
    public FinanceiroLancamentoResponseDto criarCobrancaDaConsulta(Consulta consulta, BigDecimal valor) {
        String descricao = consulta.getDescricao() == null || consulta.getDescricao().isBlank()
                ? "Consulta odontologica"
                : consulta.getDescricao();

        return criarCobrancaDaConsulta(
                consulta,
                valor,
                descricao,
                consulta.getDataInicio().toLocalDate()
        );
    }

    @Transactional
    public FinanceiroLancamentoResponseDto marcarComoPago(Long id) {
        FinanceiroLancamento lancamento = buscarLancamentoPorId(id);

        if (lancamento.getStatus() == StatusLancamentoFinanceiro.PAGO) {
            throw new RuntimeException("Cobranca ja esta paga");
        }

        if (lancamento.getStatus() == StatusLancamentoFinanceiro.CANCELADO) {
            throw new RuntimeException("Cobranca cancelada nao pode ser paga");
        }

        lancamento.setStatus(StatusLancamentoFinanceiro.PAGO);
        lancamento.setDataPagamento(LocalDateTime.now());

        return toResponseDto(repository.save(lancamento));
    }

    @Transactional
    public FinanceiroLancamentoResponseDto cancelar(Long id) {
        FinanceiroLancamento lancamento = buscarLancamentoPorId(id);

        if (lancamento.getStatus() == StatusLancamentoFinanceiro.PAGO) {
            throw new RuntimeException("Cobranca paga nao pode ser cancelada");
        }

        if (lancamento.getStatus() == StatusLancamentoFinanceiro.CANCELADO) {
            throw new RuntimeException("Cobranca ja esta cancelada");
        }

        lancamento.setStatus(StatusLancamentoFinanceiro.CANCELADO);
        lancamento.setDataPagamento(null);

        return toResponseDto(repository.save(lancamento));
    }

    private FinanceiroLancamentoResponseDto criarCobrancaDaConsulta(
            Consulta consulta,
            BigDecimal valor,
            String descricao,
            LocalDate dataVencimento
    ) {
        validarValor(valor);
        validarDescricao(descricao);

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
        lancamento.setDescricao(descricao.trim());
        lancamento.setValor(valor);
        lancamento.setDataVencimento(dataVencimento);

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

    private FinanceiroLancamento buscarLancamentoPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lancamento financeiro nao encontrado"));
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
