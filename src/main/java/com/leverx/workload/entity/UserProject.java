package com.leverx.workload.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_project")
public class UserProject {

  @EmbeddedId private UserProjectId id;

  @Column(name = "assign_date")
  private LocalDateTime assignDate;

  @Column(name = "cancel_date")
  private LocalDateTime cancelDate;
}
