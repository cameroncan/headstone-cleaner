package com.cameronchristiansen.headstonecleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class HIServiceImpl implements HIService {

	private static final Logger logger = LoggerFactory.getLogger(HIServiceImpl.class);
	
	private static final String INPUT_IMAGE_DIR = "data/input-images";
	private static final String OUTPUT_IMAGE_DIR = "data/output-images";
	
	@Override
	public HIResult getBinarizedImage(String imagePath) {
		String destinationPath = OUTPUT_IMAGE_DIR;
		String imagePathBase = INPUT_IMAGE_DIR;
		
		File image = new File(imagePathBase + "/" + imagePath);
		if (!image.exists() || !image.isFile())
		{
			throw new IllegalArgumentException("The image specified does not exist or is not a file (" + image.getAbsolutePath() + ")");
		}
		String fullImagePath = image.getAbsolutePath();
		
		File destinationDir = new File(OUTPUT_IMAGE_DIR);
		destinationDir.mkdirs();
		
		HIResult result = new HIResult();
		result.setOriginalPath(image.getPath());
		
		logger.info("Starting process for image at path: {}", fullImagePath);
		try {
			Date timerStart = new Date();
			ProcessBuilder pb = new ProcessBuilder("/home/cam/Applications/HeadstoneIndexer/HeadstoneIndexer", fullImagePath, destinationPath);
			pb.redirectErrorStream(true);
			Process process = pb.start();
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
			logger.info("Output of HeadstoneIndexer:");
			
			Date timerEnd = new Date();
			result.setDuration(timerEnd.getTime() - timerStart.getTime());
			logger.info("Finished process for image at path: {}", fullImagePath);
		} catch (IOException e) {
			logger.error("Error in running the HeadstoneIndexer", e);
		}
		
		String imageName = imagePath.substring(0, imagePath.lastIndexOf("."));
		result.setBinarizedNormalPath(destinationPath + "/finalartworkRemoval_" + imageName + ".png");
		result.setBinarizedInvertedPath(destinationPath + "/finalartworkRemoval_" + imageName + "-inverse.png");
		
		return result;
	}

	@Override
	public String storeUploadedImage(MultipartFile uploadedFile) throws IllegalStateException, IOException {
		String imageId = UUID.randomUUID().toString();

		String originalFileName = uploadedFile.getOriginalFilename();
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
		File newFile = new File(INPUT_IMAGE_DIR + "/" + imageId + fileExtension);
	
		uploadedFile.transferTo(newFile);
		return newFile.getPath();
	}

	@Override
	public List<String> getInputImages() {
		List<String> inputImages = new ArrayList<String>();
		File inputImagesDir = new File(INPUT_IMAGE_DIR);
		File[] files = inputImagesDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.matches("\\d{3}\\.JPG"))
				{
					return true;
				}
				return false;
			}
		});
		
		for (File file : files)
		{
			inputImages.add(file.getName());
		}
		
		Collections.sort(inputImages);
		return inputImages;
	}

}
