package com.test.openai.domain.yolo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DetectedInfoResponse {

    private String label;
    private float x;
    private float y;

}
