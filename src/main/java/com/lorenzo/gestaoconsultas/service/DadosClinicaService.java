package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.DadosClinicaRequestDto;
import com.lorenzo.gestaoconsultas.dto.DadosClinicaResponseDto;
import com.lorenzo.gestaoconsultas.entity.DadosClinica;
import com.lorenzo.gestaoconsultas.repository.DadosClinicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DadosClinicaService {

    private final DadosClinicaRepository repository;

    public DadosClinicaService(DadosClinicaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DadosClinicaResponseDto buscar() {
        return toResponseDto(buscarOuCriar());
    }

    @Transactional
    public DadosClinicaResponseDto atualizar(DadosClinicaRequestDto dto) {
        DadosClinica dados = buscarOuCriar();
        aplicarDados(dados, dto);

        return toResponseDto(repository.save(dados));
    }

    private DadosClinica buscarOuCriar() {
        return repository.findAll().stream()
                .findFirst()
                .orElseGet(() -> repository.save(criarPadrao()));
    }

    private DadosClinica criarPadrao() {
        DadosClinica dados = new DadosClinica();
        dados.setNomeFantasia("Dentix Clínica Odontológica");
        dados.setRazaoSocial("Dentix Clínica Odontológica");
        dados.setEmail("contato@dentix.com");
        dados.setTelefone("(11) 4002-8922");
        dados.setEndereco("Av. Principal, 1000");
        dados.setCidade("São Paulo");
        dados.setEstado("SP");
        dados.setCep("00000-000");
        dados.setResponsavelTecnico("Administrador da clínica");
        dados.setHorarioFuncionamento("Segunda a sexta, 08:00 - 18:00");
        return dados;
    }

    private void aplicarDados(DadosClinica dados, DadosClinicaRequestDto dto) {
        dados.setNomeFantasia(dto.getNomeFantasia().trim());
        dados.setRazaoSocial(limpar(dto.getRazaoSocial()));
        dados.setCnpj(limpar(dto.getCnpj()));
        dados.setEmail(limpar(dto.getEmail()));
        dados.setTelefone(limpar(dto.getTelefone()));
        dados.setEndereco(limpar(dto.getEndereco()));
        dados.setCidade(limpar(dto.getCidade()));
        dados.setEstado(limpar(dto.getEstado()));
        dados.setCep(limpar(dto.getCep()));
        dados.setResponsavelTecnico(limpar(dto.getResponsavelTecnico()));
        dados.setCroResponsavel(limpar(dto.getCroResponsavel()));
        dados.setHorarioFuncionamento(limpar(dto.getHorarioFuncionamento()));
    }

    private String limpar(String valor) {
        return valor == null || valor.trim().isEmpty() ? null : valor.trim();
    }

    private DadosClinicaResponseDto toResponseDto(DadosClinica dados) {
        return new DadosClinicaResponseDto(
                dados.getId(),
                dados.getNomeFantasia(),
                dados.getRazaoSocial(),
                dados.getCnpj(),
                dados.getEmail(),
                dados.getTelefone(),
                dados.getEndereco(),
                dados.getCidade(),
                dados.getEstado(),
                dados.getCep(),
                dados.getResponsavelTecnico(),
                dados.getCroResponsavel(),
                dados.getHorarioFuncionamento(),
                dados.getDataAtualizacao()
        );
    }
}
