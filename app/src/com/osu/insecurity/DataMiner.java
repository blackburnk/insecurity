package com.osu.insecurity;

import java.util.HashSet;
import java.util.Set;

public class DataMiner implements IDataMiner {

	// TODO - Have a system to hold all points
	// Or just grab from sql database if not in memory
	
	Set<DataPoint> points;
	int kThreshold;
	double dThreshold;
	
	/**
	 * Constructor for DataMiner.
	 * @param groupMinimumSize The minimum number of points needed to be considered not noise.
	 * @param distanceThreshold The distance to check for other points.
	 */
	public DataMiner(int groupMinimumSize, double distanceThreshold) {
		points = new HashSet<DataPoint>();
		this.kThreshold = groupMinimumSize;
		this.dThreshold = distanceThreshold;
	}
	
	public static void main(String[] args) {
		System.out.println("Hello world");
	}
	
	/**
	 * Find the distance between two points.
	 * @param point1 First point
	 * @param point2 Second point
	 * @return The distance between the two points.
	 */
	private double Distance(DataPoint point1, DataPoint point2) {
		return Math.sqrt(Math.pow((point1.latitude + point2.latitude), 2)
				       + Math.pow((point1.longitude * point2.longitude), 2));
	}
	
	/**
	 * Determines if point is noise or not.
	 * @param point - The point to classify
	 * @param distance - The distance to check for other points.
	 * @param threshold - The minimum number of points needed to be considered not noise.
	 * @return If the point is noise or not.
	 */
	private boolean IsNoise(DataPoint point, double distance, int threshold) {
		// Keep track of number of points within distance
		int numberPoints = 0;
		
		// For each point, check if within distance
		for (DataPoint p : points)
		{
			if (Distance(point, p) < distance)
			{
				numberPoints++;
			}
		}
		
		return numberPoints < threshold;
	}

	/**
	 * Classify a point into a cluster.
	 * @param point - A point that is not a noise point.
	 * @return The cluster the point belongs to.
	 */
	private int ClassifyCluster(DataPoint point) {
		
		DataPoint closestPoint = ClosestPoint(point);
		
		if (closestPoint == null)
		{
			DataPoint.maxCluster++;
			return DataPoint.maxCluster;
		}
		else
			return closestPoint.GetCluster();
	}
	
	/**
	 * Find the closest clustered point to the given point
	 * @param point - A point
	 * @return The closest already clustered point, else null
	 */
	private DataPoint ClosestPoint(DataPoint point) {
		DataPoint closestPoint = null;
		double closestDistance = 1000000;
		
		// Find the closest point to the datapoint, give same cluster
		for(DataPoint p : points) {
			//Point must be closest and not a noise point
			if (p.GetCluster() != 0 && Distance(point, p) < closestDistance){
				closestPoint = p;
				closestDistance = Distance(point, p);
			}
		}
		
		return closestPoint;
	}

	@Override
	public double AddLocation(double latitude, double longitude) {
		// All points have latitude, longitude, and cluster #
		// When adding new point
		//  *Determine if point is noise point
		//  *Determine cluster number
		//  Cluster #0 is reserved for noise points
		//  This points will be updated periodically
		//  and assigned to a cluster if/when there are enough
		//  to warrent a clustering
		
		double rawRisk = 0;
		DataPoint temp = new DataPoint(latitude, longitude, 0);
		
		// Check if point is noise
		if (!IsNoise(temp, dThreshold, kThreshold)) {
			// If not noise: Figure out the cluster it belong to
			int cluster = ClassifyCluster(temp);
			temp.SetCluster(cluster);
		}
		
		
		// TODO - Classify security level of point
		// Simple procedure for now, more complicated later
		DataPoint closestPoint = ClosestPoint(temp);
		if(closestPoint != null) {
			rawRisk = Distance(closestPoint, temp);
		} else {
			rawRisk = 100000;
		}
		
		
		
		return rawRisk;
	}

	/**
	 * Reclassify all noise points. This updates any emerging clusters
	 * from new high density noise areas.
	 */
	public void UpdateNoisePoints() {

		for (DataPoint p : points)
		{
			// For all noise points
			if (p.cluster == 0) {
				// Check if point is still noise
				if (!IsNoise(p, dThreshold, kThreshold)) {
					// If not noise: Figure out the cluster it belong to
					int cluster = ClassifyCluster(p);
					p.SetCluster(cluster);
				}
			}		
		}
	}
}
