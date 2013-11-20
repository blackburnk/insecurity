package com.osu.insecurity;

public class DataPoint {

	double latitude;
	double longitude;
	int cluster;
	
	public static int maxCluster = 0;
	
	public DataPoint(double latitude, double longitude, int cluster)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.cluster = cluster;
	}
	
	public double GetLatitude() {
		return latitude;
	}
	
	public double GetLongitude() {
		return longitude;
	}
	
	public int GetCluster() {
		return cluster;
	}
	
	public void SetCluster(int newCluster) {
		DataPoint.maxCluster = Math.max(newCluster, DataPoint.maxCluster);
		cluster = newCluster;
	}
}
