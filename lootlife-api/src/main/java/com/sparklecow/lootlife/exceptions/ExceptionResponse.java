package com.sparklecow.lootlife.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

//This class will be sent as a response to errors.
@Data
//Only a JSON will be created with attributes that are not empty or blank
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private int businessErrorCode;
    private String businessErrorDescription;
    private Set<String> validationErrors;
    private Map<String, String> errorDetails;
}