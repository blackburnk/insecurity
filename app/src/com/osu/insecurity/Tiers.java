package com.osu.insecurity;


/**
 * Enumeration for levels of reaction.
 *
 * @author Andy Shaw
 */
public enum Tiers
{
    TIER1(new int [] {Responses.ALARM.code()}),
    TIER2(new int [] {Responses.CAMERA.code(), Responses.ALARM.code(), Responses.MICROPHONE.code()}),
    TIER3(new int [] {Responses.CAMERA.code(), Responses.ALARM.code(), Responses.MICROPHONE.code(), Responses.TEXT_SECONDARY.code()}),
    TIER4(new int [] {Responses.LAUNCH_MISSILE_TO_LOCATION.code()});
    
     private int [] responses;
	 
	 Tiers(int[] responses) {
		 this.responses = responses;
	 }
	 
}