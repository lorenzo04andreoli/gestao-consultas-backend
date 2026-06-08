package com.lorenzo.gestaoconsultas.dto;

public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String perfil;
    private Boolean ativo;
    private String fotoPerfil;
    private Boolean twoFactorAtivo;

    public UsuarioResponseDto(Long id, String nome, String cpf, String email, String perfil, Boolean ativo, String fotoPerfil, Boolean twoFactorAtivo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.perfil = perfil;
        this.ativo = ativo;
        this.fotoPerfil = fotoPerfil;
        this.twoFactorAtivo = twoFactorAtivo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }
    public Boolean getAtivo() { return ativo; }
    public String getFotoPerfil() { return fotoPerfil; }
    public Boolean getTwoFactorAtivo() { return twoFactorAtivo; }

}
