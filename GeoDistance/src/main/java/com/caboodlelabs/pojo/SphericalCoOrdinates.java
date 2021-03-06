package com.caboodlelabs.pojo;

import com.google.gson.Gson;

public class SphericalCoOrdinates {
	
	private double lat;
	private double lon;
	private double height;
	
	public SphericalCoOrdinates(double lat, double lon, double height){
		this.lat = lat;
		this.lon = lon;
		this.height = height;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
