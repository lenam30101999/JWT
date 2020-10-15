package com.mcg.jwt.demo.domain.mapper;

import com.mcg.jwt.demo.app.dto.ProfileDTO;
import com.mcg.jwt.demo.domain.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    @Mappings({
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt")
    })
    ProfileDTO convertToDTO(Profile profile);
}
