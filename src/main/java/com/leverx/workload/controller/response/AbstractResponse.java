package com.leverx.workload.controller.response;

import java.io.Serializable;
import lombok.Data;

@Data
public abstract class AbstractResponse implements Serializable {
  private long id;
}
