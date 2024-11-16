package org.machine.domain.yolo.controller;

import lombok.RequiredArgsConstructor;
import org.machine.domain.yolo.dto.YoloResponse;
import org.machine.domain.yolo.service.YoloService;
import org.springframework.http.MediaType;
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
    public YoloResponse detectImage(@RequestBody byte[] imageFile) {
        return yoloService.detect(imageFile);
    }

}
