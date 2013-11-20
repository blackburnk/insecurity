package com.osu.insecurity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataMiner implements IDataMiner {

	int kThreshold;
	double dThreshold;
	
	Set<DataPoint> corePoints;
	Set<DataPoint> edgePoints;
	Set<DataPoint> noisePoints;
	
	HashMap<Integer, Set<DataPoint>> clusterMap;
	
	/**
	 * Constructor for DataMiner.
	 * @param groupMinimumSize The minimum number of points needed to be considered not noise.
	 * @param distanceThreshold The distance to check for other points.
	 */
	public DataMiner(int groupMinimumSize, double distanceThreshold) {
		corePoints = new HashSet<DataPoint>();
		edgePoints = new HashSet<DataPoint>();
		noisePoints = new HashSet<DataPoint>();
		
		clusterMap = new HashMap<Integer, Set<DataPoint>>();
		
		this.kThreshold = groupMinimumSize;
		this.dThreshold = distanceThreshold;
	}
	
	public static void main(String[] args) {
		System.out.println("This is an Implementation of DBSCAN.");
	}
	
	private void SortPoint(DataPoint point) {
		int closePoints = 0;
		boolean isBorder = false;
		
		// Count number of points within d-threshold
		
		for (DataPoint p : this.corePoints) {
			if (Distance(p, point) < this.dThreshold) {
				closePoints++;
				isBorder = true; 
			}
		}
		
		for (DataPoint p : this.edgePoints) {
			if (Distance(p, point) < this.dThreshold) {
				closePoints++;
			}
		}
		
		for (DataPoint p : this.noisePoints) {
			if (Distance(p, point) < this.dThreshold) {
				closePoints++;
			}
		}
		
		if (closePoints >= kThreshold) {
			this.corePoints.add(point);
		} else if (isBorder) {
			this.edgePoints.add(point);
		} else {
			this.noisePoints.add(point);
		}
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
	 * Find the closest clustered point to the given point
	 * @param point - A point
	 * @return The closest already clustered point, else null
	 */
	private DataPoint ClosestPoint(DataPoint point, Integer cluster) {
		DataPoint closestPoint = null;
		double closestDistance = 1000000;
		
		if (!clusterMap.containsKey(cluster)) {
			return null;
		}
		
		Set<DataPoint> points = clusterMap.get(cluster); 
		
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
	
	private void AddPointToMap(DataPoint point) {
		if (clusterMap.containsKey(point.cluster)) {
			clusterMap.get(point.cluster).add(point);
		} else {
			clusterMap.put(point.cluster, new HashSet<DataPoint>());
			clusterMap.get(point.cluster).add(point);
		}
	}

	@Override
	public double AddLocation(double latitude, double longitude) {
		// All points have latitude, longitude, and cluster #
		// Cluster 0 is noise cluster
		
		double rawRisk = 0;
		DataPoint temp = new DataPoint(latitude, longitude, 0);
		
		for (DataPoint update : edgePoints) {
			SortPoint(update);
		}
		
		for (DataPoint update : noisePoints) {
			SortPoint(update);
		}
		
		SortPoint(temp);
		
		// Now cluster points
		for (DataPoint c : this.corePoints) {
			if (c.cluster == 0) {
				DataPoint.maxCluster++;
				c.cluster = DataPoint.maxCluster;
				AddPointToMap(c);
			}
			
			for (DataPoint e : this.edgePoints) {
				if (e.cluster == 0 && Distance(c, e) < dThreshold) {
					e.cluster = DataPoint.maxCluster;
					AddPointToMap(e);
				}
			}
		}
			
		int cluster = temp.cluster;
		
		// Calculate raw risk
		DataPoint closestPoint = ClosestPoint(temp, cluster);
		
		if(closestPoint != null) {
			rawRisk = Distance(closestPoint, temp);
		} else {
			rawRisk = 100000;
		}
		
		return rawRisk;
	}
}
