package com.leverx.workload.csv.service.parser;

import com.leverx.workload.csv.service.model.CsvUser;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvRuntimeException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
@Slf4j
public class CsvUserParser {

  public List<CsvUser> parseCsv(MultipartFile multipartFile) {
    try (CSVReader reader = new CSVReader(new InputStreamReader(multipartFile.getInputStream()))) {
      List<CsvUser> users = new CsvToBeanBuilder<CsvUser>(reader).withSeparator(',')
          .withIgnoreEmptyLine(true).withType(CsvUser.class).build().parse();
      log.info("Csv file was parsed, read " + users.size() + " users from csv");
      return users;
    } catch (IOException e) {
      throw new CsvRuntimeException(e.getMessage());
    }
  }
}
