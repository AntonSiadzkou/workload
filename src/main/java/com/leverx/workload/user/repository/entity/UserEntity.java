package com.leverx.workload.user.repository.entity;

import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.security.service.model.Role;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = {"department", "projects"})
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1919086764211023149L;

  @Id
  @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 4)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
  private long id;

  @Column(name = "first_name", nullable = false)
  @NotBlank(message = "First name is required")
  @Size(min = 3, max = 255,
      message = "First name must be at least 3 characters long, but less than 255 characters")
  private String firstName;

  @Column(name = "last_name", nullable = false)
  @NotBlank(message = "Last name is required")
  @Size(min = 3, max = 255,
      message = "Last name must be at least 3 characters long, but less than 255 characters")
  private String lastName;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "Email is required")
  @Size(min = 4, max = 64,
      message = "Email must be at least 4 characters, but less than 64 characters")
  @Email(message = "Wrong email format")
  private String email;

  @Column(nullable = false)
  @NotBlank(message = "Password is required")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,64}$",
      message = "Password must contain at least one lowercase character, one uppercase character, one digit, and a length between 6 to 64")
  private String password;

  @Column(nullable = false)
  @NotBlank(message = "Position is required")
  private String position;

  @NotNull(message = "Department is required")
  @ManyToOne
  @JoinColumn(name = "department_id")
  private DepartmentEntity department;

  @Column(nullable = false)
  @NotNull(message = "Role is required")
  @Enumerated(value = EnumType.STRING)
  private Role role;

  @Column(name = "is_active", nullable = false)
  @NotNull(message = "Active status is required")
  private Boolean active;

  @ManyToMany
  @JoinTable(name = "user_project", joinColumns = @JoinColumn(name = "id_user"),
      inverseJoinColumns = @JoinColumn(name = "id_project"))
  private List<ProjectEntity> projects;

  public boolean add(ProjectEntity entity) {
    if (projects == null) {
      projects = new ArrayList<>();
    }
    return projects.add(entity);
  }
}
