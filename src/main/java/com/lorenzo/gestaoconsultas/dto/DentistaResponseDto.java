package com.lorenzo.gestaoconsultas.dto;

import java.util.List;

public class DentistaResponseDto {

    private Long id;
    private String nome;
    private String email;
    private String cro;
    private Boolean ativo;
    private List<String> especialidades;

    public DentistaResponseDto(Long id, String nome, String email,
                               String cro, Boolean ativo, List<String> especialidades) {
        this.setId(id);
        this.setNome(nome);
        this.setEmail(email);
        this.setCro(cro);
        this.setAtivo(ativo);
        this.setEspecialidades(especialidades);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }
}
