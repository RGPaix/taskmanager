package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void deveCriarTarefaComSucesso() throws IllegalAccessException {
        Task novaTask = new Task();
        novaTask.setTitle("Estudar Spring Boot");
        novaTask.setDescription("Praticar criação de APIs REST");

        Task taskSalva = new Task();
        taskSalva.setId(1L);
        taskSalva.setTitle("Estudar Spring Boot");
        taskSalva.setDescription("Praticar criação de APIs REST");

        when(taskRepository.save(novaTask)).thenReturn(taskSalva);

        Task resultado = taskService.createTask(novaTask);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Estudar Spring Boot", resultado.getTitle());
        verify(taskRepository, times(1)).save(novaTask);
    }

    @Test
    void deveLancarExcecaoAoCriarTarefaSemTitulo(){
        Task novaTask = new Task();
        novaTask.setTitle("");
        novaTask.setDescription("Sem título");

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(novaTask));
        verify(taskRepository, never()).save(any(Task.class));
    }
}
