package org.usfirst.frc.team2811.robot;
import java.lang.Math;

import edu.wpi.first.wpilibj.DriverStation;
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
		return Math.abs(a-b);
	}
	
	public static double map(double inputTicks,double inMin, double inMax, double outputMin,double outputMax){
        return (inputTicks/(inMax-inMin)-inMin/(inMax-inMin))*(outputMax-outputMin)+outputMin;
    }
	
	public static double getPreferencesDouble(String key, double backup){
		if(prefs.containsKey(key)){
			return prefs.getDouble(key, backup);
		} else {
			prefs.putDouble(key, backup);
			return backup;
		}
	}
	
	public static boolean getPreferencesBoolean(String key, boolean backup){
		if(prefs.containsKey(key)){
			return prefs.getBoolean(key, backup);
		} else {
			prefs.putBoolean(key, backup);
			return backup;
		}
	}
	
	public static double constrain(double input, double outputMax, double outputMin){
		if(input>outputMax){
			return outputMax;
		}else if(input<outputMin){
			return outputMin;
		} else {
			return input;
		}
	}
	
	public static void updateFlash(){
		Robot.blender.updateValFromFlash();
		Robot.chassis.updateValFromFlash();
		Robot.climber.updateValFromFlash();
		Robot.elevator.updateValFromFlash();
		Robot.shooter.updateValFromFlash();
		Robot.turret.updateValFromFlash();
		Robot.visionBoiler.updateValFromFlash();
		Robot.visionGear.updateValFromFlash();
	}
	
	/**
	 * Maps an input range using multiple points split into 2 independent lists. 
	 * Lists should have the same size.  
	 * <br><br>
	 * Eg given the lists {1,2,3,4,5},{10,20,30,20,10}
	 * <br>
	 * the value of 3 would map to 30, and 1.5 would map to 15
	 * 
	 * @param input	value to interpolate
	 * @param fromList list of input values
	 * @param toList list of output values
	 * @return
	 */
	public static double getMapValueFromLists(double input, double[] fromList, double[] toList){
		if(fromList.length!=toList.length){
    		System.err.println("Number of elements in the list does not match!!!");
    		return toList[0];
    	}
    	for (int i=0; i<fromList.length-1;i++){
    		if(fromList[i+1]>input){
    			return map(input, fromList[i], fromList[i+1], 
    	    			toList[i], toList[i+1]);
    		}
    	}
    	return toList[fromList.length-1];
	}

	/**
	 * Perform linear interpolation on a nested multidimensional array
	 * eg, given the list {{1,10},{2,20},{3,30},{4,20},{5,10}}
	 * converting 3 would return 30, and converting 1.5 would return 15
	 * 
	 * @param input 
	 * @param pairedlist Array of 2-element double arrays, eg [ [1,1],[2,3],[3,6] ... ]
	 * @return
	 */
	public static double getMapValueFromList(double input, double[][] pairedlist){

		if(input<pairedlist[0][0])return pairedlist[0][1];
		
    	for (int i=0; i<pairedlist.length-1;i++){
        	if(pairedlist[i+1][0]>input){
    			return map(input, 
    					pairedlist[i][0], pairedlist[i+1][0], 
    					pairedlist[i][1], pairedlist[i+1][1]
    					);
    		}
    	}
    	
    	return pairedlist[pairedlist.length-1][1];
	}
	
	//TODO put in MINIPID
	public static double pidOutputLimitAdd(double output, double minimumOutputLimit){
		
		
		if(output>-0.01  && output < 0.01){
			output = 0.0;
		}else if(output>0.0){
			output = output + minimumOutputLimit;
		}else if(output < 0.0){
			output = output - minimumOutputLimit;
		}
		
		return output;
	}

	
	/**
	 * Some weird programming stuff
	 * @return LORD DAN
	 */
	public static String warningChangeBattery(){

	return "                                                                     \n" +
			" XXXXX    X     X      XX       XX      X        XXXXX   XXXXXXX   \n"+
	"		 XX       X     X      XX       XXX     X      XXX       X          \n"+
	"		XX        X     X     XXXX      X XX    X    XXX         X			\n"+
	"		X         X     X    XX  X      X  XX   X   XX           XXXXXXXX	\n"+
	"		X         XXXXXXX    X   XX     XX  XX  X   X     XXXX    X         \n"+
	"		XX        X     X   XXXXXXXX     X   XX X   XX       X    X         \n"+
	"		 XXX      X     X   X      X     X    XXX    XX     XX    XX        \n"+
	"		   XXXXX  X     X  X       X     X     XX      XXXXXX      XXXXXXXX\n"+
	"                                                                                      \n" +
	
	
	"		                                                                       XX            X\n"+
	"		 XXXXXXXXX      XXX         XXXXXXXXXXXXXXX            XXXXXXXXXXXX     XXX         X\n"+
	"		 X       XX    XX  XXXXXXXXXXXX   XX       XXXXXXXXXX  X          XX      XXX       X\n"+
	"		 X       XX   XX   XX    X         X       X           X           XX       XXX    X\n"+
	"		X      XXX    X     X    X         X       X           X           XX         XXX X\n"+
	"		XXXXXXXXX    XX     X    X         X       X           X         XXX            XXX\n"+
	"		X       XXX  XXXXXXXXX   X         X       XXXXX       X X  XXXXX                X\n"+
	"		X         XXXX      X    X         X        X           XXXXX                   X\n"+
	"		X          XX       X    X         X        X           X   XXXX               XX\n"+
	"		X        XX X       X    X         X        X           X       XXXX           X\n"+
	"		XXXXXXXXXX XX       X    X          X       X           X           XXX       X\n"+
	"		                                            XXXXXXXXXXX X                    XX\n"+
	"		                                                                            XX\n"+
	"		                                                                           XX\n";
	
	}
	
}
