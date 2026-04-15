package com.lorenzo.gestaoconsultas.dto;

public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String email;
    private String perfil;

    public UsuarioResponseDto(Long id, String nome, String email, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }

}
