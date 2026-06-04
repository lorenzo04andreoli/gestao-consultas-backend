package com.lorenzo.gestaoconsultas.dto;

import java.time.LocalDateTime;

public class DadosClinicaResponseDto {

    private Long id;
    private String nomeFantasia;
    private String razaoSocial;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private String responsavelTecnico;
    private String croResponsavel;
    private String horarioFuncionamento;
    private LocalDateTime dataAtualizacao;

    public DadosClinicaResponseDto(Long id, String nomeFantasia, String razaoSocial, String cnpj,
                                   String email, String telefone, String endereco, String cidade,
                                   String estado, String cep, String responsavelTecnico,
                                   String croResponsavel, String horarioFuncionamento,
                                   LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.responsavelTecnico = responsavelTecnico;
        this.croResponsavel = croResponsavel;
        this.horarioFuncionamento = horarioFuncionamento;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() { return id; }
    public String getNomeFantasia() { return nomeFantasia; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getCnpj() { return cnpj; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getCep() { return cep; }
    public String getResponsavelTecnico() { return responsavelTecnico; }
    public String getCroResponsavel() { return croResponsavel; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
}
