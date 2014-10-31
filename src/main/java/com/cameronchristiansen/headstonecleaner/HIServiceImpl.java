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
	
	//private static final String INPUT_IMAGE_DIR = "data/input-images";
	//private static final String OUTPUT_IMAGE_DIR = "data/output-images";
	
	@Override
	public HIResult getBinarizedImage(String imagePath) {
		String relativePathToInputImages = "data/input-images";
		String fullPathToInputImages = System.getenv("pathToWebapp") + "/" + relativePathToInputImages;
		String relativePathToOutputImages = "data/output-images";
		String fullPathToOutputImages = System.getenv("pathToWebapp") + "/" + relativePathToOutputImages;
		String pathToExecutable = System.getenv("HeadstoneIndexerPath");
		
		logger.info("image storage path: " + fullPathToInputImages);
		
		File image = new File(fullPathToInputImages + "/" + imagePath);
		if (!image.exists() || !image.isFile())
		{
			throw new IllegalArgumentException("The image specified does not exist or is not a file (" + image.getAbsolutePath() + ")");
		}
		String fullImagePath = image.getAbsolutePath();
		
		File destinationDir = new File(fullPathToOutputImages);
		destinationDir.mkdirs();
		
		HIResult result = new HIResult();
		result.setOriginalPath(relativePathToInputImages + "/" + imagePath);
		
		logger.info("Starting process for image at path: {}", fullImagePath);
		
		if (null == pathToExecutable)
		{
			throw new RuntimeException("The executable path is not defined, please define the path in the system variable 'HeadstoneIndexerPath'");
		}
		logger.info("using path to executable: " + pathToExecutable);
		try {
			Date timerStart = new Date();
			logger.info("Calling executable with: " + fullImagePath + "  " + fullPathToOutputImages);
			ProcessBuilder pb = new ProcessBuilder(pathToExecutable + "/HeadstoneIndexer", fullImagePath, fullPathToOutputImages);//fullPathToOutputImages);
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
		result.setBinarizedNormalPath(relativePathToOutputImages + "/finalartworkRemoval_" + imageName + ".png");
		result.setBinarizedInvertedPath(relativePathToOutputImages + "/finalartworkRemoval_" + imageName + "-inverse.png");
		
		return result;
	}

	@Override
	public String storeUploadedImage(MultipartFile uploadedFile) throws IllegalStateException, IOException {
		String pathToStoreImages = System.getenv("pathToWebapp");
		String relativePathToInputImages = "data/input-images";
		String inputImagePath = pathToStoreImages + "/" + relativePathToInputImages;
		File inputImageDir = new File(inputImagePath);
		inputImageDir.mkdirs();

		String imageId = UUID.randomUUID().toString();

		String originalFileName = uploadedFile.getOriginalFilename();
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
		File newFile = new File(inputImagePath + "/" + imageId + fileExtension);
	
		uploadedFile.transferTo(newFile);
		return relativePathToInputImages + "/" + newFile.getName();
	}

	@Override
	public List<String> getInputImages() {
		String relativePathToInputImages = "data/input-images";
		String fullPathToInputImages = System.getenv("pathToWebapp") + "/" + relativePathToInputImages;
		
		List<String> inputImages = new ArrayList<String>();
		File inputImagesDir = new File(fullPathToInputImages);
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
		
		if (null == files)
		{
			logger.warn("There were no preloaded input images found, return an empty list");
			return inputImages;
		}
		
		for (File file : files)
		{
			inputImages.add(file.getName());
		}
		
		Collections.sort(inputImages);
		return inputImages;
	}

}
