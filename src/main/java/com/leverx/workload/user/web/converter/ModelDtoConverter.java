package com.leverx.workload.user.web.converter;

import com.leverx.workload.user.model.User;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.response.UserResponse;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ModelDtoConverter {
  private final ModelMapper mapper;

  public UserResponse toResponse(User model) {
    return Objects.isNull(model) ? null : mapper.map(model, UserResponse.class);
  }

  public User toModelFromRequest(UserBodyParams request) {
    return Objects.isNull(request) ? null : mapper.map(request, User.class);
  }
}
