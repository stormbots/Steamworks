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
	
	
//---------------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * 
	 * @return LORD DAN
	 */
	public static String lordDan(){

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
