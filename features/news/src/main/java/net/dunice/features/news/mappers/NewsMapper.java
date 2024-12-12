package net.dunice.features.news.mappers;

import net.dunice.features.news.dtos.reponses.CredentialsResponse;
import net.dunice.features.news.dtos.requests.NewsRequest;
import net.dunice.features.news.entities.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    @Mapping(target = "authorUsername", expression = "java(credentials.username())")
    @Mapping(target = "authorId", expression = "java(credentials.uuid())")
    @Mapping(target = "tags", ignore = true)
    NewsEntity requestToEntity(Long id, NewsRequest request, CredentialsResponse credentials);
}
