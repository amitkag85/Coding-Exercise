package com.route;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.route.model.City;
import com.route.model.CityGraph;
import com.route.model.Constants;

@RestController
public class CityController {

	@Value("${city.file.path}")
	public String filePath;

	@GetMapping("/connected")
	public String findConnectedCity(@RequestParam(value = "origin", required = false) String origin,
			@RequestParam(value = "destination", required = false) String destination) {
		String checkconnection = Constants.NO_CONNECTION_EXISTS;
		try {
			Stream<String> linesStream = Files.lines(Paths.get(filePath));

			List<City> cityList = linesStream.map(line -> populateCity(line)).collect(Collectors.toList());

			CityGraph g = new CityGraph();
			//CityGraph graph = g.populateCityGraph(cityList);
			
			checkconnection = g.checkConnection(cityList,origin.trim(),destination.trim());
			
			cityList.stream().forEach(x -> System.out.println(x));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return checkconnection;

	}



	private City populateCity(String line) {
		String[] lineSplit = line.split(",");
		City city = new City();
		city.setSource(lineSplit[0]);
		city.setDestination(lineSplit[1]);

		return city;

	}

}
