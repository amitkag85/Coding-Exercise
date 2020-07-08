package com.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.city.model.City;
import com.city.model.Constants;

public class CityGraph {
	public Map<Vertex, List<Vertex>> adjVertices;

	public CityGraph() {
		this.adjVertices = new HashMap<Vertex, List<Vertex>>();
	}

	void addVertex(String label) {
		adjVertices.putIfAbsent(new Vertex(label), new ArrayList<>());
	}

	void addEdge(String cityName1, String cityName2) {
		Vertex v1 = new Vertex(cityName1);
		Vertex v2 = new Vertex(cityName2);
		adjVertices.get(v1).add(v2);
		adjVertices.get(v2).add(v1);
	}

	List<Vertex> getAdjVertices(String label) {
		return adjVertices.get(new Vertex(label));
	}

	String printGraph() {
		StringBuffer sb = new StringBuffer();
		for (Vertex v : adjVertices.keySet()) {
			sb.append(v);
			sb.append(adjVertices.get(v));
		}
		return sb.toString();
	}

	class Vertex {
		String cityName;

		Vertex(String cityName) {
			this.cityName = cityName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vertex other = (Vertex) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (cityName == null) {
				if (other.cityName != null)
					return false;
			} else if (!cityName.equals(other.cityName))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return cityName;
		}

		private CityGraph getOuterType() {
			return CityGraph.this;
		}
	}

	

	public CityGraph populateCityGraph(List<City> cities) {
		CityGraph graph = new CityGraph();

		List<String> list = getUniqueCityNames(cities);
		for (String c : list) {
			graph.addVertex(c);
		}

		for (City city : cities) {
			graph.addEdge(city.getSource().trim(), city.getDestination().trim());
		}
		return graph;
	}

	private List<String> getUniqueCityNames(List<City> cities) {
		Set<String> set = new HashSet<>();

		List<String> list = null;

		for (City c : cities) {
			set.add(c.getDestination().trim());
			set.add(c.getSource().trim());

		}
		list = new ArrayList<String>(set);
		return list;
	}

	public String checkConnection(List<City> cityList, String origin, String destination) {
		CityGraph populateCityGraph = populateCityGraph(cityList);
		String connectionExists = checkConnection(populateCityGraph, origin, destination);
		return connectionExists;
	}



	public String checkConnection(CityGraph g, String origin, String destination) {
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