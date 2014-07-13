package com.cameronchristiansen.headstonecleaner;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface HIService {
	List<String> getInputImages();
	HIResult getBinarizedImage(String imagePath);
	String storeUploadedImage(MultipartFile uploadedFile) throws IllegalStateException, IOException;
}
