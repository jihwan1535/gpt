import base64
import os
import ssl

import cv2
import numpy as np
import torch
from flask import Flask, request, jsonify

app = Flask(__name__)

ssl._create_default_https_context = ssl._create_unverified_context


def load_model():
    base_yolo_model = os.path.join(app.static_folder, 'best.pt')
    return torch.hub.load('ultralytics/yolov5', 'custom', path=base_yolo_model)


model = load_model()


def decode_image(image_bytes):
    nparr = np.frombuffer(image_bytes, np.uint8)
    return cv2.imdecode(nparr, cv2.IMREAD_COLOR)


def detect_objects(img):
    results = model(img)
    return results.xyxy[0].cpu().numpy()


def process_detections(detections, img):
    center = []
    height, width = img.shape[:2]  # 이미지의 높이와 너비 가져오기

    for detection in detections:
        x1, y1, x2, y2, conf, cls = detection
        label = f"{model.names[int(cls)]}"

        # 중심 좌표를 백분율로 계산
        center_x = ((x1 + x2) / 2) / width
        center_y = ((y1 + y2) / 2) / height

        center.append({
            "label": label,
            "x": round(center_x, 2),  # 소수점 둘째 자리까지 반올림
            "y": round(center_y, 2)  # 소수점 둘째 자리까지 반올림
        })

        # 바운딩 박스와 라벨 그리기 (픽셀 좌표 사용)
        cv2.rectangle(img, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 2)
        cv2.putText(img, label, (int(x1), int(y1) - 10), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 255, 0), 6)

    return center, img

def encode_image_to_base64(img):
    _, img_encoded = cv2.imencode('.jpg', img)
    return base64.b64encode(img_encoded).decode('utf-8')


@app.route('/detect', methods=['POST'])
def detect_changes_api():
    if 'image/png' not in request.content_type:
        return jsonify({"error": "Invalid content type. Expected image/png"}), 400

    try:
        img = decode_image(request.data)
        detections = detect_objects(img)
        center, processed_img = process_detections(detections, img)

        image_base64 = encode_image_to_base64(processed_img)

        response_data = {
            "result": center,
            "image": image_base64
        }

        return jsonify(response_data)

    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True, port=5000)