package org.project.spring.controller;

import lombok.RequiredArgsConstructor;
import org.project.spring.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("images")
@RequiredArgsConstructor
public class ImagesController {

    private final ImageService imagesService;

    @GetMapping
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(imagesService.getAllImages());
    }

}
