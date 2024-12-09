package net.dunice.features.auth.mappers;

import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.auth.dtos.responses.AuthUserResponse;
import net.dunice.features.shared.entities.UserEntity;
import net.dunice.features.users.dtos.responses.PublicUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthUserMapper {

    @Mapping(target = "news", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", expression = "java(request.name())")
    UserEntity registerRequestToEntity(RegisterRequest request);

    AuthUserResponse publicResponseToAuth(PublicUserResponse response, String token);
}
