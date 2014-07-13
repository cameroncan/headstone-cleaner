package com.cameronchristiansen.headstonecleaner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HIController 
{
	private static final Logger logger = LoggerFactory.getLogger(HIController.class);
	@Autowired
	HIService hiService;
	
	@RequestMapping(value="inputImages", method=RequestMethod.GET)
	public ResponseEntity<List<String>> getInputImages()
	{
		List<String> imagePaths = hiService.getInputImages();
		return new ResponseEntity<List<String>>(imagePaths, HttpStatus.OK);
	}
	@RequestMapping(value="binarize", method=RequestMethod.POST)
	public ResponseEntity<HIResult> getBinarizedImage(@RequestBody String imagePath)
	{
		HIResult hiResult = hiService.getBinarizedImage(imagePath);
		return new ResponseEntity<HIResult>(hiResult, HttpStatus.OK);
	}
	
	@RequestMapping(value="upload", method=RequestMethod.POST)
	public ResponseEntity<String> getBinarizedImage(@RequestBody MultipartFile file) throws IllegalStateException, IOException
	{
		String imagePath = hiService.storeUploadedImage(file);
		return new ResponseEntity<String>(imagePath, HttpStatus.OK);
	}
}
