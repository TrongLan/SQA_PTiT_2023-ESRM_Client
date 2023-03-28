package com.example.esrmclient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SubjectClass {
  private Long id;
  private String code;
  private String name;
  private String subjectCode;
  private String lectureCode;
}
