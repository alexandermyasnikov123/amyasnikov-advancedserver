package net.dunice.features.news.mappers;

import net.dunice.features.news.dtos.requests.NewsRequest;
import net.dunice.features.shared.entities.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    NewsEntity requestToEntity(Long id, NewsRequest request);
}
