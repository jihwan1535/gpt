package org.machine.domain.yolo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DetectedObjectResponse {

    private byte[] image;
    private List<DetectedInfoResponse> result;

}
