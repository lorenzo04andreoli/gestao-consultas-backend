package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoRequestDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoResponseDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroPrecoRequestDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroPrecoResponseDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroPrecoSugestaoResponseDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroResumoResponseDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroTabelaPrecoRequestDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroTabelaPrecoResponseDto;
import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.entity.Dentista;
import com.lorenzo.gestaoconsultas.entity.Especialidade;
import com.lorenzo.gestaoconsultas.entity.FinanceiroLancamento;
import com.lorenzo.gestaoconsultas.entity.FinanceiroPreco;
import com.lorenzo.gestaoconsultas.entity.FinanceiroTabelaPreco;
import com.lorenzo.gestaoconsultas.enums.StatusConsulta;
import com.lorenzo.gestaoconsultas.enums.StatusLancamentoFinanceiro;
import com.lorenzo.gestaoconsultas.repository.ConsultaRepository;
import com.lorenzo.gestaoconsultas.repository.DentistaRepository;
import com.lorenzo.gestaoconsultas.repository.EspecialidadeRepository;
import com.lorenzo.gestaoconsultas.repository.FinanceiroLancamentoRepository;
import com.lorenzo.gestaoconsultas.repository.FinanceiroPrecoRepository;
import com.lorenzo.gestaoconsultas.repository.FinanceiroTabelaPrecoRepository;
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
    private final FinanceiroTabelaPrecoRepository tabelaPrecoRepository;
    private final FinanceiroPrecoRepository precoRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final DentistaRepository dentistaRepository;

    public FinanceiroService(FinanceiroLancamentoRepository repository,
                             ConsultaRepository consultaRepository,
                             FinanceiroTabelaPrecoRepository tabelaPrecoRepository,
                             FinanceiroPrecoRepository precoRepository,
                             EspecialidadeRepository especialidadeRepository,
                             DentistaRepository dentistaRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
        this.tabelaPrecoRepository = tabelaPrecoRepository;
        this.precoRepository = precoRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.dentistaRepository = dentistaRepository;
    }

    public List<FinanceiroLancamentoResponseDto> listarLancamentos() {
        return repository.findAllByOrderByDataCriacaoDesc().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public FinanceiroLancamentoResponseDto buscarPorId(Long id) {
        return toResponseDto(buscarLancamentoPorId(id));
    }

    public FinanceiroPrecoSugestaoResponseDto sugerirPreco(Long dentistaId, Long especialidadeId) {
        Dentista dentista = dentistaRepository.findById(dentistaId)
                .orElseThrow(() -> new RuntimeException("Dentista nao encontrado"));
        Especialidade especialidade = especialidadeRepository.findById(especialidadeId)
                .orElseThrow(() -> new RuntimeException("Especialidade nao encontrada"));

        validarEspecialidadeDoDentista(dentista, especialidade);

        return precoRepository
                .findFirstByTabelaPrecoAtivoTrueAndAtivoTrueAndEspecialidadeIdAndDentistaIdOrderByTabelaPrecoIdDesc(
                        especialidade.getId(),
                        dentista.getId()
                )
                .map(preco -> toSugestaoResponseDto(preco, "DENTISTA"))
                .or(() -> precoRepository
                        .findFirstByTabelaPrecoAtivoTrueAndAtivoTrueAndEspecialidadeIdAndDentistaIsNullOrderByTabelaPrecoIdDesc(
                                especialidade.getId()
                        )
                        .map(preco -> toSugestaoResponseDto(preco, "ESPECIALIDADE")))
                .orElseGet(() -> new FinanceiroPrecoSugestaoResponseDto(false, null, null, null, null));
    }

    public List<FinanceiroTabelaPrecoResponseDto> listarTabelasPreco() {
        return tabelaPrecoRepository.findAllByOrderByNomeAsc().stream()
                .map(this::toTabelaPrecoResponseDto)
                .toList();
    }

    @Transactional
    public FinanceiroTabelaPrecoResponseDto criarTabelaPreco(FinanceiroTabelaPrecoRequestDto dto) {
        if (tabelaPrecoRepository.existsByNomeIgnoreCase(dto.getNome().trim())) {
            throw new RuntimeException("Tabela de preco ja cadastrada");
        }

        FinanceiroTabelaPreco tabela = new FinanceiroTabelaPreco();
        tabela.setNome(dto.getNome().trim());
        tabela.setAtivo(dto.getAtivo() == null || dto.getAtivo());

        return toTabelaPrecoResponseDto(tabelaPrecoRepository.save(tabela));
    }

    @Transactional
    public FinanceiroTabelaPrecoResponseDto desativarTabelaPreco(Long id) {
        FinanceiroTabelaPreco tabela = buscarTabelaPrecoPorId(id);
        tabela.setAtivo(false);
        return toTabelaPrecoResponseDto(tabelaPrecoRepository.save(tabela));
    }

    public List<FinanceiroPrecoResponseDto> listarPrecos(Long tabelaPrecoId) {
        buscarTabelaPrecoPorId(tabelaPrecoId);

        return precoRepository.findByTabelaPrecoIdOrderByEspecialidadeNomeAsc(tabelaPrecoId).stream()
                .map(this::toPrecoResponseDto)
                .toList();
    }

    @Transactional
    public FinanceiroPrecoResponseDto criarPreco(FinanceiroPrecoRequestDto dto) {
        validarValor(dto.getValor());
        validarDescricao(dto.getDescricao());

        FinanceiroTabelaPreco tabela = buscarTabelaPrecoPorId(dto.getTabelaPrecoId());
        Especialidade especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                .orElseThrow(() -> new RuntimeException("Especialidade nao encontrada"));
        Dentista dentista = buscarDentistaOpcional(dto.getDentistaId());

        validarDuplicidadePreco(tabela.getId(), especialidade.getId(), dentista);

        FinanceiroPreco preco = new FinanceiroPreco();
        preco.setTabelaPreco(tabela);
        preco.setEspecialidade(especialidade);
        preco.setDentista(dentista);
        preco.setDescricao(dto.getDescricao().trim());
        preco.setValor(dto.getValor());
        preco.setAtivo(dto.getAtivo() == null || dto.getAtivo());

        return toPrecoResponseDto(precoRepository.save(preco));
    }

    @Transactional
    public FinanceiroPrecoResponseDto desativarPreco(Long id) {
        FinanceiroPreco preco = buscarPrecoPorId(id);
        preco.setAtivo(false);
        return toPrecoResponseDto(precoRepository.save(preco));
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

        BigDecimal aReceber = repository.somarPorStatus(StatusLancamentoFinanceiro.PENDENTE);

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

    private FinanceiroTabelaPreco buscarTabelaPrecoPorId(Long id) {
        return tabelaPrecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tabela de preco nao encontrada"));
    }

    private FinanceiroPreco buscarPrecoPorId(Long id) {
        return precoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preco nao encontrado"));
    }

    private Dentista buscarDentistaOpcional(Long dentistaId) {
        if (dentistaId == null) {
            return null;
        }

        return dentistaRepository.findById(dentistaId)
                .orElseThrow(() -> new RuntimeException("Dentista nao encontrado"));
    }

    private void validarDuplicidadePreco(Long tabelaPrecoId, Long especialidadeId, Dentista dentista) {
        boolean duplicado = dentista == null
                ? precoRepository.existsByTabelaPrecoIdAndEspecialidadeIdAndDentistaIsNullAndAtivoTrue(
                        tabelaPrecoId,
                        especialidadeId
                )
                : precoRepository.existsByTabelaPrecoIdAndEspecialidadeIdAndDentistaIdAndAtivoTrue(
                        tabelaPrecoId,
                        especialidadeId,
                        dentista.getId()
                );

        if (duplicado) {
            throw new RuntimeException("Preco ja cadastrado para esta regra");
        }
    }

    private void validarEspecialidadeDoDentista(Dentista dentista, Especialidade especialidade) {
        boolean pertenceAoDentista = dentista.getEspecialidades() != null
                && dentista.getEspecialidades().stream()
                        .anyMatch(e -> e.getId().equals(especialidade.getId()));

        if (!pertenceAoDentista) {
            throw new RuntimeException("Especialidade nao pertence ao dentista selecionado");
        }
    }

    private FinanceiroTabelaPrecoResponseDto toTabelaPrecoResponseDto(FinanceiroTabelaPreco tabela) {
        return new FinanceiroTabelaPrecoResponseDto(
                tabela.getId(),
                tabela.getNome(),
                tabela.getAtivo(),
                tabela.getDataCriacao()
        );
    }

    private FinanceiroPrecoResponseDto toPrecoResponseDto(FinanceiroPreco preco) {
        Dentista dentista = preco.getDentista();

        return new FinanceiroPrecoResponseDto(
                preco.getId(),
                preco.getTabelaPreco().getId(),
                preco.getTabelaPreco().getNome(),
                preco.getEspecialidade().getId(),
                preco.getEspecialidade().getNome(),
                dentista == null ? null : dentista.getId(),
                dentista == null ? null : dentista.getNome(),
                preco.getDescricao(),
                preco.getValor(),
                preco.getAtivo(),
                preco.getDataCriacao()
        );
    }

    private FinanceiroPrecoSugestaoResponseDto toSugestaoResponseDto(FinanceiroPreco preco, String origem) {
        return new FinanceiroPrecoSugestaoResponseDto(
                true,
                preco.getId(),
                preco.getValor(),
                preco.getDescricao(),
                origem
        );
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
