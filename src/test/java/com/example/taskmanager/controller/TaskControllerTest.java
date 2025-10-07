package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Limpa o banco antes de cada teste
        taskRepository.deleteAll();
    }

    @Test
    void deveCriarTarefaComPOST() throws Exception {
        Task novaTask = new Task();
        novaTask.setTitle("Aprender Spring Boot");
        novaTask.setDescription("Criar APIs REST com Spring Boot");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Aprender Spring Boot"))
                .andExpect(jsonPath("$.description").value("Criar APIs REST com Spring Boot"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void deveRetornarListaComGET() throws Exception {
        // Cria uma tarefa no banco
        Task task = new Task();
        task.setTitle("Tarefa de Teste");
        task.setDescription("Descrição da tarefa de teste");
        taskRepository.save(task);

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Tarefa de Teste"))
                .andExpect(jsonPath("$[0].description").value("Descrição da tarefa de teste"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }
}