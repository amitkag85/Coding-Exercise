package com.city.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.city.model.City;
import com.city.model.Constants;
import com.city.service.CityService;

/**
 * 
 * @author NeeraKumar
 *
 */
@RestController
public class CityController {

	@Value("${city.file.path}")
	public String filePath;
	
    private static final Logger LOG = LoggerFactory.getLogger(CityController.class);

	
	@Autowired
	CityService cityService;

	@GetMapping("/connected")
	public String checkConnectedCity(@RequestParam(value = "origin", required = false) String origin,
			@RequestParam(value = "destination", required = false) String destination) {
		String checkconnection = Constants.NO_CONNECTION_EXISTS;
		try (Stream<String> linesStream = Files.lines(Paths.get(filePath))) {
			List<City> cityList = linesStream.map(line -> populateCity(line)).collect(Collectors.toList());
			checkconnection = cityService.checkConnection(cityList, origin, destination);
		} catch (IOException e) {
			LOG.error("Exception Occured checkConnectedCity :{}",e.getMessage());
		}

		return checkconnection;

	}

	private City populateCity(String line) {
		String[] lineSplit = line.split(",");
		City city = new City();
		city.setSource(lineSplit[0].trim());
		city.setDestination(lineSplit[1].trim());
		return city;

	}

}
