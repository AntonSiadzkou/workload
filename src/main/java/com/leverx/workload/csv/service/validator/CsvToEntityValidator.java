package com.leverx.workload.csv.service.validator;

import com.leverx.workload.csv.service.model.CsvUser;
import com.leverx.workload.department.repository.DepartmentRepository;
import com.leverx.workload.security.service.model.Role;
import com.leverx.workload.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CsvToEntityValidator {

  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;

  private static final String ONLY_LATIN_LETTERS = "^[a-zA-Z]+$";
  private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,64}$";
  private static final String EMAIL_PATTERN =
      "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

  public List<CsvUser> validateCsvUsersToEntities(List<CsvUser> csvUsers) {
    List<CsvUser> users = csvUsers.stream()
        .filter(user -> user.getFirstName().matches(ONLY_LATIN_LETTERS))
        .filter(user -> user.getLastName().matches(ONLY_LATIN_LETTERS))
        .filter(user -> user.getPosition().matches(ONLY_LATIN_LETTERS))
        .filter(user -> user.getPassword().matches(PASSWORD_PATTERN))
        .filter(user -> user.getEmail().matches(EMAIL_PATTERN))
        .filter(user -> departmentRepository.findById(user.getDepartment()).isPresent())
        .filter(user -> userRepository.findByEmail(user.getEmail()).isEmpty()).filter(user -> Arrays
            .stream(Role.values()).anyMatch(role -> role.name().equals(user.getRole())))
        .toList();
    log.info("Parsed users from csv successfully filtered by user entity restrictions");
    return users;
  }
}
