package com.cameronchristiansen.headstonecleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HIServiceImpl implements HIService {

	private static final Logger logger = LoggerFactory.getLogger(HIServiceImpl.class);
	
	@Override
	public HIResult getBinarizedImage(String imagePath) {
		//TODO use database to store images and to generate the id
		String id = UUID.randomUUID().toString();
		String destinationPath = "data/output-images/" + id;
		String imagePathBase = "data/input-images";
		
		File image = new File(imagePathBase + "/" + imagePath);
		if (!image.exists() || !image.isFile())
		{
			throw new IllegalArgumentException("The image specified does not exist or is not a file (" + image.getAbsolutePath() + ")");
		}
		String fullImagePath = image.getAbsolutePath();
		
		File destinationDir = new File(destinationPath);
		destinationDir.mkdirs();
		
		HIResult result = new HIResult(id);
		result.setOriginalPath(image.getPath());
		
		logger.info("Starting process for image at path: {}", fullImagePath);
		try {
			Date timerStart = new Date(); 
			Process process = new ProcessBuilder("/home/cam/Applications/HeadstoneIndexer/HeadstoneIndexer", fullImagePath, destinationPath).start();
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
		
		//TODO set these paths
		String imageName = imagePath.substring(0, imagePath.lastIndexOf(".JPG"));
		result.setBinarizedNormalPath(destinationPath + "/finalartworkRemoval_" + imageName + ".png");
		result.setBinarizedInvertedPath(destinationPath + "/finalartworkRemoval_" + imageName + "-inverse.png");
		
		return result;
	}

}
