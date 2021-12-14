package com.leverx.workload.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserProjectId implements Serializable {

  @Column(name = "id_user", nullable = false)
  private long userId;

  @Column(name = "id_project", nullable = false)
  private long projectId;
}
