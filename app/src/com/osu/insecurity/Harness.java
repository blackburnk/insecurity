package com.osu.insecurity;

public class Harness {
	
	DataMiner miner;
	
	public Harness()
	{
		// Create dataminer
		// This keeps hold of all the points as well as additional info
		miner = new DataMiner(7, 1.0/69.0); // 7 points minimum for cluster
											// 1 mile max range between points in cluster
				
		// TODO - Instantiate whatever else here
	}
	
	public String[] AddPoint(double Longitude, double Latitude)
	{
		// Get raw value expressed as Degrees. Convert under assumption 1 degree ~= 60 miles.
		double raw = miner.AddLocation(Latitude, Longitude);
		
		// Raw conversion to error level
        double warning = raw*60;
        
        // create a response to send to phone
        return Responder.createResponse(warning);
	}

	public void FalseAlarmAddPoint(double Latitude, double Longitude)
    {
        // Get raw value expressed as Degrees. Convert under assumption 1 degree ~= 60 miles.
        double notNeeded = miner.AddLocation(Latitude, Longitude);
        
        // do not create a response for the phone
    }
	
}
