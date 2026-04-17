package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.DentistaResponseDto;
import com.lorenzo.gestaoconsultas.entity.Dentista;
import com.lorenzo.gestaoconsultas.repository.DentistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistaService {

    private final DentistaRepository repository;

    public DentistaService(DentistaRepository repository) {
        this.repository = repository;
    }

    public Dentista salvar(Dentista dentista){
        repository.findByEmail(dentista.getEmail()).ifPresent(d -> {throw new RuntimeException("Email já cadastrado");} );;

        repository.findByCpf(dentista.getCpf()).ifPresent(d -> {throw new RuntimeException("CPF já cadastrado");} );;

        repository.findByCro(dentista.getCro()).ifPresent(d -> {throw new RuntimeException("CRO já cadastrado");} );;

        return repository.save(dentista);
    }

    public List<DentistaResponseDto> listar() {
        return repository.findAll().stream()
                .map(d -> new DentistaResponseDto(
                        d.getId(),
                        d.getNome(),
                        d.getEmail(),
                        d.getCro(),
                        d.getAtivo(),
                        d.getEspecialidades()
                                .stream()
                                .map(e -> e.getNome())
                                .toList()
                ))
                .toList();
    }

    public Dentista buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
    }

    public Dentista atualizar(Long id, Dentista dentista) {
        Dentista existente = buscarPorId(id);

        existente.setNome(dentista.getNome());
        existente.setEmail(dentista.getEmail());
        existente.setCpf(dentista.getCpf());
        existente.setCro(dentista.getCro());
        existente.setAtivo(dentista.getAtivo());

        return repository.save(existente);
    }

     public void deletar(Long id) {
        repository.deleteById(id);
    }
}
