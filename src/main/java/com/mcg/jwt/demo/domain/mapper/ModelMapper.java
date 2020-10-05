package com.mcg.jwt.demo.domain.mapper;

import com.mcg.jwt.demo.app.dto.ProfileDTO;
import com.mcg.jwt.demo.domain.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    @Mapping(target = "user", ignore = true)
    Profile convertToProfile(ProfileDTO profileDTO);
}
