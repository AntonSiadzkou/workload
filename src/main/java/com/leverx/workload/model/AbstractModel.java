package com.leverx.workload.model;

import java.io.Serializable;
import lombok.Data;

@Data
public abstract class AbstractModel implements Serializable {
  private long id;
}
