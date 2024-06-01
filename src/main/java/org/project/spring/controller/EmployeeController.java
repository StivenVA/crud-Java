package org.project.spring.controller;

import lombok.RequiredArgsConstructor;
import org.project.spring.service.EmployeeService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/employees")
@RequiredArgsConstructor
@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> findAll(){

        byte[] videoData = employeeService.findById(10).getVideo();

        ByteArrayResource resource = new ByteArrayResource(videoData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("video/mp4"));
        headers.setContentDispositionFormData("attachment", "video.mp4");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
