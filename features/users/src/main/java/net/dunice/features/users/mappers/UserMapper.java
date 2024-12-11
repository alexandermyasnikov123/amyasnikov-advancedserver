package net.dunice.features.users.mappers;

import net.dunice.features.users.dtos.requests.UserRequest;
import net.dunice.features.users.dtos.responses.UserResponse;
import net.dunice.features.users.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "name", expression = "java(entity.getUsername())")
    UserResponse mapToResponse(UserEntity entity);

    @Mapping(target = "username", expression = "java(request.name())")
    UserEntity mapToEntity(UUID uuid, UserRequest request);
}
