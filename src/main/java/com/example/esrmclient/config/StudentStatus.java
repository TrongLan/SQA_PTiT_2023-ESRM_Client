package com.example.esrmclient.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudentStatus {
  UNDEFINED (0, "N/A"),
  ELIGIBLE (1, "Đủ điều kiện dự thi"),
  INELIGIBLE (2, "Không đủ điều kiện dự thi");
  private final Integer statusCode;
  private final String statusDescription;
}
