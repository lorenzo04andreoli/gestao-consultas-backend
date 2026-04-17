package com.lorenzo.gestaoconsultas.service;


import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;

    public ConsultaService(ConsultaRepository repository) {
        this.repository = repository;
    }

    public Consulta agendar(Consulta consulta){

        //Não pode ser no passado
        if (consulta.getDataInicio().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Data de início não pode ser no passado");
        }

        //dataFim menor que início
        if(consulta.getDataFim().isBefore(consulta.getDataInicio())){
            throw new RuntimeException("Data de fim não pode ser antes da data de início");
        }

        //conflito de horário
        boolean conflito = repository.existsByDentistaAndDataInicioBetween(
                consulta.getDentista(),
                consulta.getDataInicio(),
                consulta.getDataFim()
        );

        if (conflito){
            throw new RuntimeException("Dentista já possui uma consulta nesse horário");
        }

        return repository.save(consulta);
    }

    public List<Consulta> listar(String perfil, Long usuarioId, Long dentistaId) {

        if (perfil.equals("DENTISTA")) {
            return repository.findByDentistaId(dentistaId);
        }

        return repository.findAll();
    }

    public Consulta cancelar(Long id, String motivo) {

        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        if (motivo == null || motivo.isEmpty()) {
            throw new RuntimeException("Motivo é obrigatório");
        }

        consulta.setStatus("CANCELADA");
        consulta.setMotivoCancelamento(motivo);

        return repository.save(consulta);
    }
}
