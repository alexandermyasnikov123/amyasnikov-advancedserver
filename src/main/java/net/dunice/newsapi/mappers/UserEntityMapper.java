package net.dunice.newsapi.mappers;

import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "password", expression = "java(encoder.encode(request.password()))")
    @Mapping(target = "username", expression = "java(request.name())")
    UserEntity registerRequestToEntity(RegisterRequest request, PasswordEncoder encoder);

    @Mapping(target = "id", source = "entity.uuid")
    @Mapping(target = "role", expression = "java(entity.getRole())")
    @Mapping(target = "name", expression = "java(entity.getUsername())")
    PublicUserResponse entityToPublicResponse(UserEntity entity);

    @Mapping(target = "password", expression = "java(passwordHash)")
    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "username", expression = "java(request.name())")
    UserEntity updateRequestToEntity(UUID uuid, String passwordHash, UpdateUserRequest request);

    @Mapping(target = "token", source = "token")
    AuthUserResponse publicResponseToAuth(PublicUserResponse response, String token);
}
