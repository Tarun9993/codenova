package com.tarun.codenova.user.dto;

import com.tarun.codenova.common.enums.Roles;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {

    private Long id;
    private String username;
    private String email;
    private Roles role;
}
