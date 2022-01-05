package com.leverx.workload.csv.service;

import com.leverx.workload.csv.web.dto.CsvResponse;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface CsvService {

  CsvResponse loadUsers(@NotNull MultipartFile file);
}
