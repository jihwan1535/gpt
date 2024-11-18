package com.roborobo.domain.yolo.controller;

import com.roborobo.domain.yolo.dto.YoloResponse;
import com.roborobo.domain.yolo.service.YoloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/yolo")
@RequiredArgsConstructor
public class YoloController {

    private final YoloService yoloService;

    @PostMapping(value = "/detect", consumes = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<YoloResponse> detectImage(@RequestBody byte[] imageFile) {
        return ResponseEntity.ok(yoloService.detect(imageFile));
    }

}
