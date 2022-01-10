package com.leverx.workload.security.web.dto;

import javax.validation.constraints.NotNull;

public record AuthRequest(@NotNull String email, @NotNull String password){
}
