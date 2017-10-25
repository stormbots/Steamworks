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
	 * Returns an absolute difference between two numbers
	 */
	public static double difference(double a,double b){
		return Math.abs(a-b);
	}
	
	/**
	 * 
	 * @param input
	 * @param inMin
	 * @param inMax
	 * @param outputMin
	 * @param outputMax
	 * @return linear map function for conversion from a unit to another
	 */
	public static double map(double input,double inMin, double inMax, double outputMin,double outputMax){
        return (input/(inMax-inMin)-inMin/(inMax-inMin))*(outputMax-outputMin)+outputMin;
    }
	
	/**
	 * Function to set up a preference variable double
	 * @param key
	 * @param backup
	 * @return 
	 */
	public static double getPreferencesDouble(String key, double backup){
		if(prefs.containsKey(key)){
			return prefs.getDouble(key, backup);
		} else {
			prefs.putDouble(key, backup);
			return backup;
		}
	}
	
	/**
	 * Function to set up a preference variable boolean
	 * @param key
	 * @param backup
	 * @return
	 */
	public static boolean getPreferencesBoolean(String key, boolean backup){
		if(prefs.containsKey(key)){
			return prefs.getBoolean(key, backup);
		} else {
			prefs.putBoolean(key, backup);
			return backup;
		}
	}
	
	/**
	 * 
	 * @param input
	 * @param outputMax
	 * @param outputMin
	 * @return number within a range given in the passed values 
	 */
	public static double constrain(double input, double outputMax, double outputMin){
		if(input>outputMax){
			return outputMax;
		}else if(input<outputMin){
			return outputMin;
		} else {
			return input;
		}
	}
	
	/**
	 * Update all subsystems in smartdashboard
	 */
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
	
	//TODO put in MINIPID\
	/**
	 * 
	 * @param output
	 * @param minimumOutputLimit
	 * @return pid output that actually moves the robot
	 */
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
	 * Update the preference for two separate but related double lists using a String in the format of 
	 * value1<separator2>value2<separator1>value1<separator2>value2<separator1>... NO SPACE IN BETWEEN! 
	 * Example: if separator1 is , and separator2 is : the resulting format will be 
	 * value1:value2,value1:value2,value1:value2...
	 * @param key: the preference key, type has to be a String
	 * @param list1: list containing the first value of each pair
	 * @param list2: list containing the second value of each pair
	 * @param separator1: separates the String into pairs of values in the form of value1<separator2>value2
	 * @param separator2: separate each pairs of values
	 */
    private void updateMap(String key, double[] list1, double[] list2, String separator1, String separator2){
    	String[] valuePairString = prefs.getString(key, "").split(separator1);
    	//To handle the case of the new list might have more pairs than original, make a new array
    	double[] newList1 = new double[valuePairString.length];
    	double[] newList2 = new double[valuePairString.length];
    	for(int i = 0; i < valuePairString.length; i++){
    		String[] pair = valuePairString[i].split(separator2);
    		newList1[i] = Double.parseDouble(pair[0]);
    		newList2[i] = Double.parseDouble(pair[1]);
    	}
    	list1 = newList1;
    	list2 = newList2;
    }
    
 // If the battery voltage is lower than it should, force a disable in teleop and autonomous
 	public static void checkBatteryVoltage(){
 		if(Robot.PDP.getVoltage() < 11.0){
 			System.out.println("LOW BATTERY VOLTAGE");
 			//throw new RuntimeException(Util.warningChangeBattery());
 		}
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
