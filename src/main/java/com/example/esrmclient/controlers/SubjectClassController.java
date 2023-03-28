package com.example.esrmclient.controlers;

import com.example.esrmclient.config.StudentStatus;
import com.example.esrmclient.dtos.subjectclass.ClassDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SubjectClassController {
  private final RestTemplate restTemplate;

  @GetMapping(value = "/list-class/{lectureCode}")
  public String getClassesOfLecture(
      Model model,
      @PathVariable String lectureCode,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    String url = "http://localhost:8080/subject-class/%s";
    StringBuilder urlBuilder = new StringBuilder(url);
    if (StringUtils.hasText(code) || StringUtils.hasText(name)) {
      urlBuilder.append("?");
      if (StringUtils.hasText(code)) urlBuilder.append(String.format("subjectCode=%s", code));
      if (StringUtils.hasText(name)) urlBuilder.append(String.format("subjectName=%s", name));
    }
    List<ClassDTO> classDTOS =
        List.of(
            Objects.requireNonNull(
                restTemplate
                    .getForEntity(
                        String.format(urlBuilder.toString(), lectureCode), ClassDTO[].class)
                    .getBody()));
    model.addAttribute("classes", classDTOS);
    return "listClassesOfLecture";
  }
}
