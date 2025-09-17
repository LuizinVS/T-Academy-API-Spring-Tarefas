package com.example.demo.controllers;


import com.example.demo.dto.TarefaDTO;
import com.example.demo.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public List<TarefaDTO> listarTarefas() {
        return tarefaService.listarTarefas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> buscarTarefaPorId(@PathVariable Long id) {
        TarefaDTO tarefa = tarefaService.buscarTarefaPorId(id);
        if (tarefa != null) {
            return ResponseEntity.ok(tarefa);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<TarefaDTO> criarTarefa(@RequestBody TarefaDTO tarefaDTO) {
        if (tarefaDTO.getTitulo() == null || tarefaDTO.getStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        TarefaDTO novaTarefa = tarefaService.criarTarefa(tarefaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> atualizarTarefa(@PathVariable Long id, @RequestBody TarefaDTO tarefaDTO) {
        TarefaDTO tarefaAtualizada = tarefaService.atualizarTarefa(id, tarefaDTO);
        if (tarefaAtualizada != null) {
            return ResponseEntity.ok(tarefaAtualizada);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTarefa(@PathVariable Long id) {
        if (tarefaService.excluirTarefa(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/pesquisa")
    public ResponseEntity<List<TarefaDTO>> buscarPorStatus(@RequestParam String status) {
        List<TarefaDTO> tarefas = tarefaService.buscarPorStatus(status);
        return ResponseEntity.ok(tarefas);
    }
}
