package com.leverx.workload.project.repository.entity;

import com.leverx.workload.user.repository.entity.UserEntity;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
public class ProjectEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -8429149646392566543L;

  @Id
  @SequenceGenerator(name = "projects_id_seq", sequenceName = "projects_id_seq", allocationSize = 4)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_id_seq")
  private long id;

  @Column(name = "name", nullable = false)
  @NotBlank(message = "Name is required")
  private String name;

  @Column(name = "start_date")
  @NotNull
  private LocalDate startDate;

  @Column(name = "end_date")
  @NotNull
  private LocalDate endDate;

  @ManyToMany
  @JoinTable(name = "user_project", joinColumns = @JoinColumn(name = "id_project"),
      inverseJoinColumns = @JoinColumn(name = "id_user"))
  private List<UserEntity> users;

  public boolean add(UserEntity entity) {
    if (users == null) {
      users = new ArrayList<>();
    }
    return users.add(entity);
  }
}
