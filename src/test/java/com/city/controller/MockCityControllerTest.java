package com.city.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.city.model.City;
import com.city.service.CityConnectionCheckService;
import com.city.service.CityGraph;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ CityController.class })
@PropertySource("classpath:application.properties")
public class MockCityControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CityConnectionCheckService cityService;

	@Value("${city.file.path}")
	public static String filePath = "src/test/resources/city.txt";

	@MockBean
	CityGraph cityGraph;

	static List<City> citiesList = new ArrayList<>();

	@BeforeAll
	public static void setUp() throws IOException {

		System.out.println("--" + filePath);
		try (Stream<String> linesStream = Files.lines(Paths.get(filePath))) {
			citiesList = linesStream.map(line -> populateCity(line)).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void checkConnectedCity() throws Exception {
		String origin = "Boston";
		String destination = "Newark";
		given(cityService.checkConnection(citiesList, origin, destination)).
			willReturn("Yes");

		 mockMvc.perform(get("/connected?").
				contentType(MediaType.APPLICATION_JSON).param("origin", origin)
				.param("destination", destination))
		 		.andExpect(content().string(containsString("yes")));
	}

	private static City populateCity(String line) {
		String[] lineSplit = line.split(",");
		City city = new City();
		city.setSource(lineSplit[0].trim());
		city.setDestination(lineSplit[1].trim());
		return city;

	}

}
