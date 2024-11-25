package com.example.todolist.mapper;


import com.example.todolist.dto.TaskDTO;
import com.example.todolist.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskDTO dto);

    TaskDTO toDTO(Task entity);

    List<Task> toEntity(List<TaskDTO> dtos);

    List<TaskDTO> toDTO(List<Task> entities);
}
