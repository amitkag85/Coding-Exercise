package com.city.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.city.model.City;
import com.city.service.CityService;

/**
 * This class get Http Request for checking the connected cities.
 * @author NeeraKumar
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc 
public class CityControllerTest {
	
	
	private int port=8080;
	
	private static final Logger LOG = LoggerFactory.getLogger(CityControllerTest.class);


	@Autowired
	private TestRestTemplate restTemplate;
	
	private String baseUrl = "http://localhost:";
	private String testUrl1 = baseUrl+port+"/connected?origin=Boston&destination=Newark";
	private String testUrl2 = baseUrl+port+"/connected?origin=Boston&destination=Philadelphia";
	private String testUrl3 = baseUrl+port+"/connected?origin=Philadelphia&destination=Albany";
	List<City> citiesList = new ArrayList<>();
	public static final String filePath = "src/test/resources/city.txt";

	@Test
	public void checkCityConnection() throws Exception {
		assertThat(this.restTemplate.getForObject(testUrl1,String.class)).contains("Yes");
		assertThat(this.restTemplate.getForObject(testUrl2,String.class)).contains("Yes");
		assertThat(this.restTemplate.getForObject(testUrl3,String.class)).contains("No");

	}
	
	
	@BeforeEach
	public void setUp() {
		try (Stream<String> linesStream = Files.lines(Paths.get(filePath))) {
			citiesList = linesStream.map(line -> populateCity(line)).collect(Collectors.toList());
		} catch (IOException e) {
			LOG.error("Exception Occured setUp :{}",e.getMessage());
		}

	}

	
	@Test
	public void checkConnectionTest1() {
		String origin = "Boston";
		String destination = "Newark";
		CityService cityService = new CityService();
		String checkConnection = cityService.checkConnection(citiesList, origin, destination);
		assertTrue(checkConnection.equals("Yes"));
	}

	@Test
	public void checkConnectionTest2() {
		String origin = "Boston";
		String destination = "Philadelphia";
		CityService cityService = new CityService();
		String checkConnection = cityService.checkConnection(citiesList, origin, destination);
		assertTrue(checkConnection.equals("Yes"));
	}

	@Test
	public void checkConnectionTest3() {
		String origin = "Philadelphia";
		String destination = "Albany";
		CityService cityService = new CityService();
		String checkConnection = cityService.checkConnection(citiesList, origin, destination);
		assertTrue(checkConnection.equals("No"));
	}

	private City populateCity(String line) {
		String[] lineSplit = line.split(",");
		City city = new City();
		city.setSource(lineSplit[0].trim());
		city.setDestination(lineSplit[1].trim());
		return city;

	}

	
	
}
