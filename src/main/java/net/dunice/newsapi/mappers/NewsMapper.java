package net.dunice.newsapi.mappers;

import net.dunice.newsapi.dtos.requests.NewsRequest;
import net.dunice.newsapi.entities.NewsEntity;
import net.dunice.newsapi.entities.TagEntity;
import net.dunice.newsapi.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "tags", source = "tags")
    NewsEntity requestToEntity(NewsRequest request, UserEntity user, List<TagEntity> tags);
}
