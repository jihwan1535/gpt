from flask import Flask, request, send_file
import cv2
import numpy as np
from io import BytesIO
import os

app = Flask(__name__)


def detect_changes(image1, image2):
    # 이미지 크기가 다를 경우 리사이즈
    if image1.shape != image2.shape:
        image2 = cv2.resize(image2, (image1.shape[1], image1.shape[0]))
    # 그레이스케일로 변환
    gray1 = cv2.cvtColor(image1, cv2.COLOR_BGR2GRAY)
    gray2 = cv2.cvtColor(image2, cv2.COLOR_BGR2GRAY)
    # 두 이미지의 차이 계산
    diff = cv2.absdiff(gray1, gray2)
    # 임계값 적용
    _, threshold = cv2.threshold(diff, 30, 255, cv2.THRESH_BINARY)
    # 노이즈 제거
    kernel = np.ones((5, 5), np.uint8)
    threshold = cv2.morphologyEx(threshold, cv2.MORPH_OPEN, kernel)
    # 윤곽선 찾기
    contours, _ = cv2.findContours(threshold, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    # 변화가 감지된 부분 표시 및 라벨링
    result = image1.copy()
    for i, contour in enumerate(contours, 1):
        if cv2.contourArea(contour) > 100:  # 작은 변화는 무시
            x, y, w, h = cv2.boundingRect(contour)
            cv2.rectangle(result, (x, y), (x + w, y + h), (0, 255, 0), 2)
            # 라벨 추가 (숫자)
            cv2.putText(result, str(i), (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)
    return result


@app.route('/detect_changes', methods=['POST'])
def detect_changes_api():
    # 기존 이미지 (static 폴더에 저장된 이미지)
    baseImage_path = os.path.join(app.static_folder, 'baseImage.png')
    baseImage = cv2.imread(baseImage_path)

    if baseImage is None:
        return "Base image not found", 404

    # 클라이언트로부터 받은 이미지
    if 'image/png' not in request.content_type:
        return "Invalid content type. Expected image/png", 400

    image_bytes = request.data
    nparr = np.frombuffer(image_bytes, np.uint8)
    image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    if image is None:
        return "Invalid image data", 400

    # 변화 감지
    result = detect_changes(image, baseImage)

    # 결과 이미지를 바이트 스트림으로 변환
    _, buffer = cv2.imencode('.png', result)
    io_buf = BytesIO(buffer)

    return send_file(io_buf, mimetype='image/png')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)