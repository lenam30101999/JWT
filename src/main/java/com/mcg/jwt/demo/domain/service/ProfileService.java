package com.mcg.jwt.demo.domain.service;

import com.mcg.jwt.demo.app.dto.ProfileDTO;
import com.mcg.jwt.demo.domain.entity.Profile;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Objects;

@Log4j2
@Service
@Transactional
public class ProfileService extends BaseService{

    public ResponseEntity<?> getProfileById(String header){
        String accessToken = getJwtFromRequest(header);
        int id = tokenProvider.getIdFromSubjectJWT(accessToken);
        Profile profile = profileRepository.findById(id);
        ProfileDTO profileDTO = modelMapper.convertToDTO(profile);
        if (Objects.nonNull(profileDTO)){
            return utils.createOkResponse(profileDTO);
        }else return utils.createResponse("User not exist!", HttpStatus.NOT_FOUND);
    }

    private String getJwtFromRequest(String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
