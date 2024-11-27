package net.dunice.newsapi.mappers;

import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {

    @Mapping(target = "news", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", expression = "java(request.name())")
    UserEntity registerRequestToEntity(RegisterRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", expression = "java(entity.getUsername())")
    PublicUserResponse entityToPublicResponse(UserEntity entity);

    @Mapping(target = "password", expression = "java(passwordHash)")
    @Mapping(target = "username", expression = "java(request.name())")
    UserEntity updateRequestToEntity(UUID uuid, String passwordHash, UpdateUserRequest request);

    AuthUserResponse publicResponseToAuth(PublicUserResponse response, String token);
}
