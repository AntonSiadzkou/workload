package com.leverx.workload.department.repository.entity;

import com.leverx.workload.user.repository.entity.UserEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "departments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class DepartmentEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 5089699574676090585L;

  @Id
  @SequenceGenerator(name = "departments_id_seq", sequenceName = "departments_id_seq",
      allocationSize = 4)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_id_seq")
  private long id;

  @Column(name = "title", nullable = false, unique = true)
  @NotBlank(message = "Title is required")
  private String title;

  @OneToMany(mappedBy = "department")
  private List<UserEntity> users = new ArrayList<>(); // todo fix how to avoid NPE correctly

}
