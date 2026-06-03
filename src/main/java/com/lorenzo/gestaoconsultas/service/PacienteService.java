package com.lorenzo.gestaoconsultas.service;


import com.lorenzo.gestaoconsultas.entity.Paciente;
import com.lorenzo.gestaoconsultas.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public Paciente salvar(Paciente paciente){

        repository.findByEmail(paciente.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email já cadastrado");
        });

        repository.findByCpf(paciente.getCpf())
                .ifPresent(u -> {
                    throw new RuntimeException("CPF já cadastrado");
                });

        return repository.save(paciente);
    }

    public List<Paciente> listar() {
        return repository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    public Paciente atualizar(Long id, Paciente paciente) {
        Paciente existente = buscarPorId(id);

        existente.setNome(paciente.getNome());
        existente.setEmail(paciente.getEmail());
        existente.setCpf(paciente.getCpf());
        existente.setTelefone(paciente.getTelefone());
        existente.setAtivo(paciente.getAtivo() == null || paciente.getAtivo());

        return repository.save(existente);
    }

    public Paciente desativar(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setAtivo(false);
        return repository.save(paciente);
    }

    public Paciente reativar(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setAtivo(true);
        return repository.save(paciente);
    }

     public Paciente deletar(Long id) {
        return desativar(id);
    }
}
