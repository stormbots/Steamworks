package org.usfirst.frc.team2811.robot;
import java.lang.Math;

import edu.wpi.first.wpilibj.Preferences;
/**
	Utility functions for various simple maths and other helpful stuff
 */
public class Util {
	
	private static Preferences prefs = Preferences.getInstance();
	
	/**
	 * Returns a difference between two numbers
	 */
	public static double difference(double a,double b){
		return  Math.abs(Math.abs(a)-Math.abs(b));
		
		
	}
	
	public static double map(double inputTicks,double inMin, double inMax, double outputMin,double outputMax){
        return (inputTicks/(inMax-inMin)-inMin/(inMax-inMin))*(outputMax-outputMin)+outputMin;
    }
	

	public static double getPreferencesDouble(String key, double backup){
		if(prefs.containsKey(key)){
			return prefs.getDouble(key, backup);
		}
		else{
			prefs.putDouble(key, backup);
			return backup;
		}
	}
	
	public static boolean getPreferncesBoolean(String key, boolean backup){
		if(prefs.containsKey(key)){
			return prefs.getBoolean(key, backup);
		}else{
			prefs.putBoolean(key, backup);
			return backup;
		}
	}
	


	
	
	
}
