package com.leverx.workload.project.web.dto.request;

public record ProjectRequestParams(String startDate, String endDate, int page, int size, String sortColumn, String sortDirection) {

}
