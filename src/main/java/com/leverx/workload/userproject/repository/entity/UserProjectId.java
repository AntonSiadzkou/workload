package com.leverx.workload.userproject.repository.entity;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.user.repository.entity.UserEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.StringJoiner;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectId implements Serializable {

  @Serial
  private static final long serialVersionUID = -1342677087209635897L;

  @NotNull(message = "User is required")
  @ManyToOne(targetEntity = UserEntity.class)
  @JoinColumn(name = "id_user")
  private UserEntity user;

  @NotNull(message = "Project is required")
  @ManyToOne(targetEntity = ProjectEntity.class)
  @JoinColumn(name = "id_project")
  private ProjectEntity project;

  @Override
  public String toString() {
    return new StringJoiner(", ", UserProjectId.class.getSimpleName() + "[", "]")
        .add("user=" + user.getId()).add("project=" + project.getId()).toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserProjectId that = (UserProjectId) o;
    if (user != null ? !(user.getId() == that.user.getId()) : that.user != null) {
      return false;
    }
    return project != null ? (project.getId() == that.project.getId()) : that.project == null;
  }

  @Override
  public int hashCode() {
    int result = user != null ? ((int) (user.getId() ^ (user.getId() >>> 32))) : 0;
    result =
        31 * result + (project != null ? ((int) (project.getId() ^ (project.getId() >>> 32))) : 0);
    return result;
  }
}
