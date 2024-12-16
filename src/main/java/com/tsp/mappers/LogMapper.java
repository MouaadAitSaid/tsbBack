package com.tsp.mappers;

import com.tsp.dtos.LogInputDTO;
import com.tsp.dtos.LogOutputDTO;
import com.tsp.models.Log;
import com.tsp.models.Task;
public class LogMapper {

    // Map LogInputDTO to Log Entity
    public static Log toEntity(LogInputDTO dto, Task task) {
        return new Log(
                task,
                dto.action(),
                dto.oldTitle(),
                dto.newTitle(),
                dto.oldDescription(),
                dto.newDescription(),
                dto.oldStatus(),
                dto.newStatus(),
                dto.oldVersion(),
                dto.newVersion()
        );
    }

    // Map Log Entity to LogOutputDTO
    public static LogOutputDTO toOutputDTO(Log log) {
        return new LogOutputDTO(
                log.getId(),
                log.task().getId(),
                log.action(),
                log.timestamp(),
                log.oldTitle(),
                log.newTitle(),
                log.oldDescription(),
                log.newDescription(),
                log.oldStatus(),
                log.newStatus(),
                log.oldVersion(),
                log.newVersion()
        );
    }
}
