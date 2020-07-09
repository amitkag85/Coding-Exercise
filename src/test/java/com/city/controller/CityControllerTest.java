package com.city.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc 
public class CityControllerTest {
	
	@LocalServerPort
	private int port=8080;

	@Autowired
	private TestRestTemplate restTemplate;
	
	private String baseUrl = "http://localhost:";
	private String testUrl1 = baseUrl+port+"/connected?origin=Boston&destination=Newark";
	private String testUrl2 = baseUrl+port+"/connected?origin=Boston&destination=Philadelphia";
	private String testUrl3 = baseUrl+port+"/connected?origin=Philadelphia&destination=Albany";


	@Test
	public void checkCityConnection() throws Exception {
	
		assertThat(this.restTemplate.getForObject(testUrl1,String.class)).contains("Yes");
		assertThat(this.restTemplate.getForObject(testUrl2,String.class)).contains("Yes");

		assertThat(this.restTemplate.getForObject(testUrl3,String.class)).contains("No");

	}
}
