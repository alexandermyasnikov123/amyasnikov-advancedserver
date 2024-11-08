package net.dunice.newsapi.mappers;

import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.UserResponse;
import net.dunice.newsapi.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {

    @Mapping(target = "role", expression = "java(new net.dunice.newsapi.entities.RoleEntity(request.role()))")
    UserEntity requestToEntity(RegisterRequest request);

    @Mapping(target = "id", source = "entity.uuid")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "role", expression = "java(entity.getRole().getName())")
    UserResponse entityToResponse(UserEntity entity, String token);
}
