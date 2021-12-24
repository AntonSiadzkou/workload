package com.leverx.workload.user.web.dto.request;

public record UserRequestParams(String firstName,  String email,  int page,  int size,
                                String sortColumn, String sortDirection){
}
