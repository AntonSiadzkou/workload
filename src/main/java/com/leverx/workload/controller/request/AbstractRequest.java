package com.leverx.workload.controller.request;

import java.io.Serializable;
import lombok.Data;

@Data
public abstract class AbstractRequest implements Serializable {
  private long id;
}
