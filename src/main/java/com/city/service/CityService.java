package com.city.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.city.model.City;
import com.city.model.Constants;
import com.city.service.CityGraph.Vertex;

@Service
public class CityService {

	@Autowired
	CityGraph cityGraph;


	public String checkConnection(List<City> cityList, String origin, String destination) {
		CityGraph populateCityGraph = populateCityGraph(cityList);
		String connectionExists = checkConnection(populateCityGraph, origin, destination);
		return connectionExists;
	}
	
	/**
	 * Prepare the Graph data structure for Cities.
	 * @param cities
	 * @return CityGraph
	 */
	private CityGraph populateCityGraph(List<City> cities) {
		CityGraph graph = new CityGraph();

		List<String> list = getUniqueCityNames(cities);
		for (String c : list) {
			graph.addVertex(c);
		}

		for (City city : cities) {
			graph.addEdge(city.getSource(), city.getDestination());
		}
		return graph;
	}

	private List<String> getUniqueCityNames(List<City> cities) {
		Set<String> set = new HashSet<>();
		List<String> uniqueCityList = null;
		for (City c : cities) {
			set.add(c.getDestination());
			set.add(c.getSource());

		}
		uniqueCityList = new ArrayList<String>(set);
		return uniqueCityList;
	}

	private String checkConnection(CityGraph g, String origin, String destination) {
		String connectionExists = Constants.NO_CONNECTION_EXISTS;
		List<Vertex> adjVertices2 = g.getAdjVertices(origin);
		for (Vertex s : adjVertices2) {
			if (s.cityName.equals(destination)) {
				connectionExists = Constants.YES_CONNECTION_EXISTS;
				return connectionExists;
			}
		}
		connectionExists = checkIndirectConnection(g, origin, destination);
		return connectionExists;
	}

	private String checkIndirectConnection(CityGraph g, String origin, String destination) {
		Map<Vertex, List<Vertex>> adjVertices3 = g.adjVertices;

		for (Map.Entry<Vertex, List<Vertex>> entry : adjVertices3.entrySet()) {

			List<Vertex> value = entry.getValue();
			boolean originPresent = value.stream().anyMatch(x -> x.cityName.equals(origin));
			boolean destinationPresent = value.stream().anyMatch(x -> x.cityName.equals(destination));

			if (originPresent && destinationPresent) {
				return Constants.YES_CONNECTION_EXISTS;
			}

		}

		return Constants.NO_CONNECTION_EXISTS;

	}

}
