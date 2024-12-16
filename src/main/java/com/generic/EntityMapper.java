package com.generic;

public interface EntityMapper<Entity, InputDTO, OutputDTO> {
    Entity toEntity(InputDTO inputDTO);
    OutputDTO toOutputDTO(Entity entity);
}
