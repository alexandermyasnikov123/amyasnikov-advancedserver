package net.dunice.features.users.mappers;

import net.dunice.features.shared.entities.UserEntity;
import net.dunice.features.users.dtos.requests.UpdateUserRequest;
import net.dunice.features.users.dtos.responses.PublicUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PublicUserMapper {
    @Mapping(target = "name", expression = "java(entity.getUsername())")
    PublicUserResponse entityToPublicResponse(UserEntity entity);

    @Mapping(target = "password", expression = "java(passwordHash)")
    @Mapping(target = "username", expression = "java(request.name())")
    UserEntity updateRequestToEntity(UUID uuid, String passwordHash, UpdateUserRequest request);
}
