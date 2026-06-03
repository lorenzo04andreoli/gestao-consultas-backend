package com.lorenzo.gestaoconsultas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String cpf;

    private String telefone;

    private Boolean ativo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;


    public Paciente() {}

    public Paciente(Long id, String nome, String email, String cpf, String telefone, Boolean ativo, LocalDateTime dataCriacao) {
        this.setId(id);
        this.setNome(nome);
        this.setEmail(email);
        this.setCpf(cpf);
        this.setTelefone(telefone);
        this.setAtivo(ativo);
        this.setDataCriacao(dataCriacao);
    }

    @PrePersist
    public void prePersist() {
        this.setDataCriacao(LocalDateTime.now());
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
