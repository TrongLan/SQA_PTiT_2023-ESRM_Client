package com.example.esrmclient.controlers;

import com.example.esrmclient.config.RestResponsePage;
import com.example.esrmclient.config.StudentStatus;
import com.example.esrmclient.dtos.PaginationData;
import com.example.esrmclient.dtos.studentclass.StudentDetailDTO;
import com.example.esrmclient.dtos.studentclass.StudentScoreDTO;
import com.example.esrmclient.dtos.subjectclass.ComponentScoreDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping(value = "/student")
@RequiredArgsConstructor
public class StudentController {
  private final RestTemplate restTemplate;

  @GetMapping(value = "/list/{classCode}")
  public String getStudentsInClass(
      Model model,
      @PathVariable String classCode,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String page) {
    String urlStudentList = String.format("http://localhost:8080/student/%s?size=30", classCode);
    StringBuilder urlBuilder = new StringBuilder(urlStudentList);
    if (StringUtils.hasText(code)) urlBuilder.append(String.format("&studentCode=%s", code));
    if (StringUtils.hasText(name)) urlBuilder.append(String.format("&fullName=%s", name));
    if (StringUtils.hasText(status)) urlBuilder.append(String.format("&status=%s", status));
    int pagenum;
    try {
      pagenum = Integer.parseInt(page);
    } catch (Exception e) {
      pagenum = 1;
    }
    if (StringUtils.hasText(page)) urlBuilder.append(String.format("&page=%s", pagenum - 1));

    ParameterizedTypeReference<RestResponsePage<StudentDetailDTO>> reference =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<RestResponsePage<StudentDetailDTO>> response =
        restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, null, reference);
    RestResponsePage<StudentDetailDTO> body = response.getBody();
    List<StudentDetailDTO> studentDetailDTOS = Objects.requireNonNull(body).getContent();
    model.addAttribute("students", studentDetailDTOS);

    PaginationData paginationData = new PaginationData();
    paginationData.setTotalPages(body.getTotalPages());
    paginationData.setNumber(body.getNumber());
    paginationData.setSize(body.getSize());
    paginationData.setTotalElements((int) body.getTotalElements());
    model.addAttribute("page", paginationData);

    String componentScoreClassURL =
        String.format("http://localhost:8080/subject-class/component-score/%s", classCode);
    List<ComponentScoreDetail> scoreDetails =
        List.of(
            Objects.requireNonNull(
                restTemplate.getForObject(componentScoreClassURL, ComponentScoreDetail[].class)));
    model.addAttribute("componentScores", scoreDetails);

    model.addAttribute("studentStatus", List.of(StudentStatus.values()));

    String getComponentScoreUrl =
        String.format("http://localhost:8080/subject-class/student-score/%s", classCode);
    ParameterizedTypeReference<Map<String, Map<Long, Double>>> reference1 =
        new ParameterizedTypeReference<>() {};
    Map<String, Map<Long, Double>> scoreMap =
        restTemplate.exchange(getComponentScoreUrl, HttpMethod.GET, null, reference1).getBody();
    model.addAttribute("scoreMap", scoreMap);
    model.addAttribute("subjectClassCode", classCode);
    model.addAttribute("dto", new StudentScoreDTO());
    return "studentListInClass";
  }

  @PostMapping(value = "/score-update")
  public String updateStudentScore(@ModelAttribute StudentScoreDTO dto) {
    Map<Long, Float> mapTemp =
        dto.getScoreMap().entrySet().stream()
            .filter(longFloatEntry -> longFloatEntry.getValue() != null)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    dto.setScoreMap(mapTemp);
    String updateStudentScoreEndpoint = "http://localhost:8080/student/score";
    restTemplate.postForObject(updateStudentScoreEndpoint, dto, StudentScoreDTO.class);
    log.info("studentCode: {}, classCode: {}", dto.getStudentCode(), dto.getClassCode());
    return "redirect:/student/list/%s".formatted(dto.getClassCode());
  }
}
