package com.leverx.workload.csv.service.impl;

import com.leverx.workload.csv.service.CsvService;
import com.leverx.workload.csv.service.model.CsvUser;
import com.leverx.workload.csv.service.parser.CsvUserParser;
import com.leverx.workload.csv.service.validator.CsvToEntityValidator;
import com.leverx.workload.csv.web.dto.CsvResponse;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.converter.UserConverter;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class CsvServiceImpl implements CsvService {

  private final UserRepository userRepository;
  private final UserConverter mapper;
  private final CsvUserParser parser;
  private final CsvToEntityValidator validator;

  @Override
  @Transactional
  public CsvResponse loadUsers(@NotNull MultipartFile file) {
    CsvResponse response = new CsvResponse();
    log.info("Start parsing file");
    List<CsvUser> csvUsers = parser.parseCsv(file);
    response.setAllLines(csvUsers.size());
    log.info("Start validate parsed csv users to entity");
    List<CsvUser> filteredCvsUsers = validator.validateCsvUsersToEntities(csvUsers);
    List<UserEntity> userEntities = filteredCvsUsers.stream().map(mapper::fromCsvToEntity).toList();
    log.info("Start saving user entities to database");
    List<UserEntity> savedUsers = userRepository.saveAll(userEntities);
    response.setSavedUsers(savedUsers.size());
    response.setInvalidUsers(csvUsers.size() - savedUsers.size());
    log.info("Successfully saved " + savedUsers.size() + " user in database from csv");
    return response;
  }
}
