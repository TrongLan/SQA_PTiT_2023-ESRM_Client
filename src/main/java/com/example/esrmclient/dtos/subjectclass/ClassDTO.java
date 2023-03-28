package com.example.esrmclient.dtos.subjectclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassDTO {
  private String code;
  private String subjectCode;
  private String groupNumber;
  private String lectureCode;
  private String lectureName;
  private String subjectName;
  private int studentNumber;

  //  public ClassDTO(SubjectClass subjectClass, Subject subject, Lecture lecture) {
  //    this.code = subjectClass.getCode();
  //    this.subjectCode = subjectClass.getSubjectCode();
  //    this.groupNumber = subjectClass.getName();
  //    this.lectureCode = subjectClass.getLectureCode();
  //    this.lectureName = String.format("%s %s", lecture.getLastName(), lecture.getFirstName());
  //    this.subjectName = subject.getName();
  //  }
}
