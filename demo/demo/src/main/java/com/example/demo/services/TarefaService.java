package com.example.demo.services;

import com.example.demo.dto.TarefaDTO;
import com.example.demo.models.Tarefa;
import com.example.demo.repositories.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<TarefaDTO> listarTarefas() {
        return tarefaRepository.findAll().stream()
                .map(t -> new TarefaDTO(t.getId(), t.getTitulo(), t.getDescricao(), t.getStatus(), t.getDataCriacao(), t.getPrazo()))
                .collect(Collectors.toList());
    }

    public TarefaDTO buscarTarefaPorId(Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        return tarefa.map(t -> new TarefaDTO(t.getId(), t.getTitulo(), t.getDescricao(), t.getStatus(), t.getDataCriacao(), t.getPrazo()))
                .orElse(null);
    }

    public TarefaDTO criarTarefa(TarefaDTO tarefaDTO) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setStatus(tarefaDTO.getStatus());
        tarefa.setDataCriacao(LocalDateTime.now());
        tarefa.setPrazo(tarefaDTO.getPrazo());

        if (tarefa.getPrazo() != null && tarefa.getPrazo().isBefore(tarefa.getDataCriacao())) {
            throw new IllegalArgumentException("O prazo não pode ser anterior à data de criação.");
        }
        tarefa = tarefaRepository.save(tarefa);
        return new TarefaDTO(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getStatus(), tarefa.getDataCriacao(), tarefa.getPrazo());
    }

    public TarefaDTO atualizarTarefa(Long id, TarefaDTO tarefaDTO) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(id);
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();

            tarefa.setTitulo(tarefaDTO.getTitulo());
            tarefa.setDescricao(tarefaDTO.getDescricao());
            tarefa.setStatus(tarefaDTO.getStatus());
            tarefa.setPrazo(tarefaDTO.getPrazo());

            if (tarefa.getPrazo() != null && tarefa.getPrazo().isBefore(tarefa.getDataCriacao())) {
                throw new IllegalArgumentException("O prazo não pode ser anterior à data de criação.");
            }

            tarefa = tarefaRepository.save(tarefa);
            return new TarefaDTO(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getStatus(), tarefa.getDataCriacao(), tarefa.getPrazo());
        }
        return null;
    }

    public boolean excluirTarefa(Long id) {
        if (tarefaRepository.existsById(id)) {
            tarefaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<TarefaDTO> buscarPorStatus(String status) {
        List<Tarefa> tarefas = tarefaRepository.findByStatus(status);
        return tarefas.stream()
                .map(t -> new TarefaDTO(t.getId(), t.getTitulo(), t.getDescricao(), t.getStatus(), t.getDataCriacao(), t.getPrazo()))
                .collect(Collectors.toList());
    }
}