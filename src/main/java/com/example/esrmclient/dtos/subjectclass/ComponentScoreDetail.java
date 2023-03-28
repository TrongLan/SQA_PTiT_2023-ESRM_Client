package com.example.esrmclient.dtos.subjectclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComponentScoreDetail {
  private Long id;
  private String componentScoreCode;
  private String componentScoreName;
  private Integer weight;
}
