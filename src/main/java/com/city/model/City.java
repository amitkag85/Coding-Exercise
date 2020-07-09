package com.city.model;

/**
 * Model Class for City Object.
 * @author NeeraKumar
 *
 */
public class City {

	private String source;
	private String destination;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "City [source=" + source + ", destination=" + destination + "]";
	}
	
	

}
