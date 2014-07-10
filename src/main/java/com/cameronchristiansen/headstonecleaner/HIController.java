package com.cameronchristiansen.headstonecleaner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HIController 
{
	private static final Logger logger = LoggerFactory.getLogger(HIController.class);
	@Autowired
	HIService hiService;
	
	@RequestMapping(value="binarize", method=RequestMethod.POST)
	public ResponseEntity<HIResult> getBinarizedImage(@RequestBody String imagePath)
	{
		HIResult hiResult = hiService.getBinarizedImage(imagePath);
		return new ResponseEntity<HIResult>(hiResult, HttpStatus.OK);
	}
}
