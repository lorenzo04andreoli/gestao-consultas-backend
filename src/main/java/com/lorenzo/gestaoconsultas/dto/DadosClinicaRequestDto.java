package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DadosClinicaRequestDto {

    @NotBlank
    private String nomeFantasia;

    private String razaoSocial;

    @Size(max = 18)
    private String cnpj;

    @Email
    private String email;

    private String telefone;

    private String endereco;

    private String cidade;

    @Size(max = 2)
    private String estado;

    private String cep;

    private String responsavelTecnico;

    private String croResponsavel;

    private String horarioFuncionamento;

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    public String getResponsavelTecnico() {
        return responsavelTecnico;
    }

    public String getCroResponsavel() {
        return croResponsavel;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }
}
