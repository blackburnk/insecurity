package com.osu.insecurity;

public interface IDataMiner {
	
	/**
	 * Add a location to the current set of points.
	 * @param latitude The x value of the point
	 * @param longitude The y value of the point
	 * @return The raw warning level of adding the new point.
	 */
	double AddLocation(double latitude, double longitude);
}
