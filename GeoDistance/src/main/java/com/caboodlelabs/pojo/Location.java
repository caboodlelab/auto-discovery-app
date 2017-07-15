package com.caboodlelabs.pojo;

import com.caboodlelabs.GeoLocationUtility;
import com.google.gson.Gson;

public class Location {
	
	public Location(double id, double lat, double lon, double height){
		this.id = id;
		this.s = new SphericalCoOrdinates(lat, lon, height);
		
		this.c = GeoLocationUtility.converttoCart(s);
		
	}

	private double id;
	private SphericalCoOrdinates s;
	private CartCoOrdinates c;
	
	public double getId() {
		return id;
	}
	public void setId(double id) {
		this.id = id;
	}
	public SphericalCoOrdinates getS() {
		return s;
	}
	public void setS(SphericalCoOrdinates s) {
		this.s = s;
	}
	public CartCoOrdinates getC() {
		return c;
	}
	public void setC(CartCoOrdinates c) {
		this.c = c;
	}
	
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
