package com.test.openai.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageUploader {

	public static final String SYSTEM_PATH = System.getProperty("user.dir");
	public static final String SLASH = File.separator;
	public static final String IMAGE_DIR = SLASH + "images" + SLASH;

	private final String url;
	private final String uploadDirPath;

	public ImageUploader(
			@Value("${image.upload.url}") final String url,
			@Value("${image.upload.directory}") final String uploadDirPath
	) {
		this.url = url;
		this.uploadDirPath = uploadDirPath;
	}

	public String upload(final MultipartFile image) {
		try {
			return upload(image.getBytes(), image.getOriginalFilename());
		} catch (IOException e) {
			throw new RuntimeException("Failed to read MultipartFile", e);
		}
	}

	public String upload(final byte[] imageData, String originalFilename) {
		final String fileSavePath = SYSTEM_PATH + SLASH + uploadDirPath;
		makeDirectory(fileSavePath);

		final String saveFileName = parseSaveFileName(originalFilename);
		final File uploadPath = new File(fileSavePath, saveFileName);

		writeFile(imageData, uploadPath);
		return url + IMAGE_DIR + saveFileName;
	}

	private void makeDirectory(final String fileSavePath) {
		final File directory = new File(fileSavePath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	private String parseSaveFileName(final String originalFilename) {
		final String extension = StringUtils.getFilenameExtension(originalFilename);
		final String fileBaseName = UUID.randomUUID().toString().substring(0, 8);

		return fileBaseName + "_" + System.currentTimeMillis() + "." + extension;
	}

	private void writeFile(final byte[] imageData, final File uploadPath) {
		try (FileOutputStream fos = new FileOutputStream(uploadPath)) {
			fos.write(imageData);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write image file", e);
		}
	}

}