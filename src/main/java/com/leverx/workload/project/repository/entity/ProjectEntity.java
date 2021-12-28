package com.leverx.workload.project.repository.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
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

  @Column(name = "start_date", updatable = false)
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;
}
