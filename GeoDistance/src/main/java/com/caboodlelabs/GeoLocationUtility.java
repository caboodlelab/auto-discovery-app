package com.caboodlelabs;

import com.caboodlelabs.pojo.CartCoOrdinates;
import com.caboodlelabs.pojo.SphericalCoOrdinates;

public class GeoLocationUtility {
	
	public static double distance(SphericalCoOrdinates s1, SphericalCoOrdinates s2){
		
		CartCoOrdinates c1 = converttoCart(s1);
		CartCoOrdinates c2 = converttoCart(s2);
		
		return distance(c1,c2);
	}

	public static double distance(CartCoOrdinates c1, CartCoOrdinates c2){
		
		double dx = c1.getX() - c2.getX();
		double dy = c1.getY() - c2.getY();
		double dz = c1.getZ() - c2.getZ();
		
		return Math.sqrt((dx*dx) + (dy*dy) + (dz*dz));
	}
	
	public static CartCoOrdinates converttoCart(SphericalCoOrdinates s) {
		
		double latrad = (s.getLat() * Math.PI / 180.0);
		double lonrad = (s.getLon() * Math.PI / 180.0);
		
		double radius =  earthRadiusInMeters(latrad);
		double clatrad = geocentricLatitude(latrad);
		
		double cosLon = Math.cos(lonrad);
		double sinLon = Math.sin(lonrad);
		double cosLat = Math.cos(clatrad);
		double sinLat = Math.sin(clatrad);
		
		double x = radius * cosLon * cosLat;
		double y = radius * sinLon * cosLat;
		double z = radius * sinLat;
		
		double cosGlat = Math.cos(latrad);
		double sinGlat = Math.sin(latrad);
		
		double nx = cosGlat * cosLon;
		double ny = cosGlat * sinLon;
		double nz = sinGlat;
		
		x += (nx * s.getHeight());
		y += (ny * s.getHeight());
		z += (nz * s.getHeight());
		
		return new CartCoOrdinates(x, y, z);
	}
	
	public static double earthRadiusInMeters(double latitudeRadians){
		
		double a = 6378137.0;
		double b = 6356752.3;
		
		double cos = Math.cos (latitudeRadians);
		double sin = Math.sin (latitudeRadians);
		
		double t1 = a * a * cos;
		double t2 = b * b * sin;
		double t3 = a * cos;
		double t4 = b * sin;
		
		return Math.sqrt ((t1*t1 + t2*t2) / (t3*t3 + t4*t4));
		
	}
	
	private static double geocentricLatitude(double geographicLatitude) {
		
		double e2 = 0.00669437999014;
		double clat = Math.atan((1.0 - e2) * Math.tan(geographicLatitude));
		return clat;
    }

}
