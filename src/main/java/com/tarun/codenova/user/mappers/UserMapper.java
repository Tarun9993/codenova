package com.tarun.codenova.user.mappers;

import com.tarun.codenova.user.dto.RegistrationDto;
import com.tarun.codenova.user.dto.ResponseDto;
import com.tarun.codenova.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User dtoToEntity(RegistrationDto registrationDto){
        return User.builder()
                .email(registrationDto.getEmail())
                .username(registrationDto.getUsername())
                .build();
    }

    public ResponseDto entityToResponse(User user){
        return ResponseDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRoles())
                .build();
    }
}
