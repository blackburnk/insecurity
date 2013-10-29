package com.osu.insecurity;

/**
 * Enumeration for responses sent to the phone
 *
 * @author Andy Shaw
 */
public enum Responses
{
    ALARM(0),
    CAMERA(1),
    MICROPHONE(2),
    TEXT_SECONDARY(3),
    LAUNCH_MISSILE_TO_LOCATION(4);
    
    private int code;
    Responses(int code)
    {
    	this.code = code;
    }
    
    public int code()
    {
    	return code;
    }
}