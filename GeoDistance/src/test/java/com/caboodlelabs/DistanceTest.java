package com.caboodlelabs;

import org.junit.Test;

import com.caboodlelabs.pojo.Location;

import org.junit.Assert;

public class DistanceTest {
	
	@Test
	public void DistanceTest1(){
		
		Location p1 = new Location(1, 0, 0, 0);
		Location p2 = new Location(2, 0, 0, 0);
				
		double dist = GeoLocationUtility.distance(p1.getC(), p2.getC());
		
		Assert.assertEquals(dist, 0.0, 0.1);
	}
	
	@Test
	public void DistanceTest2(){
		
		Location p1 = new Location(1, 0, 0, 0);
		Location p2 = new Location(2, 0, 0, 10);
				
		double dist = GeoLocationUtility.distance(p1.getC(), p2.getC());
		
		Assert.assertEquals(dist, 10.0, 0.1);
	}
	
	@Test
	public void DistanceTest3(){
		
		Location p1 = new Location(1, 1.350014, 103.926641, 0);
		Location p2 = new Location(2, 1.330742, 103.878929, 0);
				
		double dist = GeoLocationUtility.distance(p1.getC(), p2.getC());
		System.out.println(dist);
		Assert.assertEquals(dist/1000, 5.72, 0.5);
	}
	
	@Test
	public void DistanceTest4(){
		
		Location p2 = new Location(1, 1.337916, 103.769742, 0);
		Location p1 = new Location(2, 1.330742, 103.878929, 0);
				
		double dist = GeoLocationUtility.distance(p1.getC(), p2.getC());
		System.out.println(dist);
		Assert.assertEquals(dist/1000, 12.22, 0.2);
	}

}
