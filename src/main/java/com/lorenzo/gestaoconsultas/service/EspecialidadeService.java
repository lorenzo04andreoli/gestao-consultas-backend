package com.lorenzo.gestaoconsultas.service;


import com.lorenzo.gestaoconsultas.entity.Especialidade;
import com.lorenzo.gestaoconsultas.repository.EspecialidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository repository;

    public EspecialidadeService(EspecialidadeRepository repository) {
        this.repository = repository;
    }

    public Especialidade salvar(Especialidade especialidade) {
        validarNome(especialidade.getNome());
        validarDuplicidade(null, especialidade.getNome());
        especialidade.setNome(especialidade.getNome().trim());

        return repository.save(especialidade);
    }

    public List<Especialidade> listar() {
        return repository.findAll();
    }

    public Especialidade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade nao encontrada"));
    }

    public Especialidade atualizar(Long id, Especialidade especialidade) {
        Especialidade existente = buscarPorId(id);

        validarNome(especialidade.getNome());
        validarDuplicidade(id, especialidade.getNome());

        existente.setNome(especialidade.getNome().trim());

        return repository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Especialidade especialidade = buscarPorId(id);

        if (especialidade.getDentistas() != null && !especialidade.getDentistas().isEmpty()) {
            throw new RuntimeException("Especialidade vinculada a dentistas nao pode ser excluida");
        }

        repository.delete(especialidade);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RuntimeException("Nome da especialidade e obrigatorio");
        }
    }

    private void validarDuplicidade(Long idAtual, String nome) {
        repository.findByNomeIgnoreCase(nome.trim())
                .filter(e -> idAtual == null || !e.getId().equals(idAtual))
                .ifPresent(e -> {
                    throw new RuntimeException("Especialidade ja cadastrada");
                });
    }
}
