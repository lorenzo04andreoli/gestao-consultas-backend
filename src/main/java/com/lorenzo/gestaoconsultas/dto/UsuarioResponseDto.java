package com.lorenzo.gestaoconsultas.dto;

public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String perfil;
    private Boolean ativo;

    public UsuarioResponseDto(Long id, String nome, String cpf, String email, String perfil, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }
    public Boolean getAtivo() { return ativo; }

}
