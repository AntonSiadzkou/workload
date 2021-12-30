package com.leverx.workload.userproject.repository.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProjectEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 2106419823189407903L;

  @EmbeddedId
  private UserProjectId id;

  @Column(name = "assign_date")
  private LocalDate assignDate;

  @Column(name = "cancel_date")
  private LocalDate cancelDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserProjectEntity entity = (UserProjectEntity) o;
    return Objects.equals(id, entity.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
