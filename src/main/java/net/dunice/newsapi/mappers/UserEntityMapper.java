package net.dunice.newsapi.mappers;

import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.UserResponse;
import net.dunice.newsapi.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {

    @Mapping(target = "password", expression = "java(encoder.encode(request.password()))")
    UserEntity requestToEntity(RegisterRequest request, PasswordEncoder encoder);

    @Mapping(target = "id", source = "entity.uuid")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "role", expression = "java(entity.getRole())")
    UserResponse entityToResponse(UserEntity entity, String token);
}
