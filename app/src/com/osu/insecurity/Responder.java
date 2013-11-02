package com.osu.insecurity;


    /**
     * Responder class to process the warning from the fencing algorithm and create a response
     * level to be sent to the phone.
     *     
     * @author Andy Shaw
     * @author Jeff Schobelock
     */
public class Responder
{
    /**
     * Create a response based on the warning sent in.  This will be a warning percentage
     * so it will be warning/100.
     * 0  < warning <= .30 -> respond with a tier 1 warning
     * .3 < warning <= .70 -> respond with a tier 2 warning
     * .7 < warning respond with a tier 3 warning
     *
     * @param warning warning level
     * @return Level of warning to execute.
     */
     public static String[] createResponse(double warning)
     {
        // possible responses
        String[] tier1 = {"ALARM"};
        String[] tier2 = {"ALARM", "CAMERA", "MICROPHONE"};
        String[] tier3 = {"ALARM", "CAMERA", "MICROPHONE", "TEXT_SECONDARY"};
        String[] tier4 = {"LAUNCH_MISSILE_TO_LOCATION"};
     
        // convert to percentage
        double risk = warning / 100;
        
        // create warning level
        if(risk > 0 && risk <= .30)
        {
            return tier1;
        }
        else if(risk > .3 && risk <= .7)
        {
            return tier2;
        }
        else
        {
            return tier3;
        }
     }
}