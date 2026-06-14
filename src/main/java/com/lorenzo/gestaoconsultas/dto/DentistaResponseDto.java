package com.lorenzo.gestaoconsultas.dto;

import java.util.List;

public class DentistaResponseDto {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String cro;
    private Boolean ativo;
    private Long usuarioId;
    private String fotoPerfil;
    private List<String> especialidades;

    public DentistaResponseDto(Long id, String nome, String email,
                               String cro, Boolean ativo, List<String> especialidades) {
        this(id, nome, null, email, cro, ativo, null, null, especialidades);
    }

    public DentistaResponseDto(Long id, String nome, String cpf, String email,
                               String cro, Boolean ativo, Long usuarioId, List<String> especialidades) {
        this(id, nome, cpf, email, cro, ativo, usuarioId, null, especialidades);
    }

    public DentistaResponseDto(Long id, String nome, String cpf, String email,
                               String cro, Boolean ativo, Long usuarioId, String fotoPerfil,
                               List<String> especialidades) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cro = cro;
        this.ativo = ativo;
        this.usuarioId = usuarioId;
        this.fotoPerfil = fotoPerfil;
        this.especialidades = especialidades;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }
}
