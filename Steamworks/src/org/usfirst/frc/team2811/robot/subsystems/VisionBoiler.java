package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.UpdateVisionBoiler;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionBoiler extends Subsystem {

	private NetworkTable networkTable;
	private Preferences prefs;
	private double distanceTarget = 0;	
	private double angleTargetHorizontal = 0;
	private double visionTimestamp = 0;
	private double robotTimestamp = 0;
	private boolean isEnabled;
	private double visionValidTargetTimeout;

    public VisionBoiler() {
    	networkTable=NetworkTable.getTable("vision");
    	prefs = Preferences.getInstance();
    	visionValidTargetTimeout=0;
	}

    public void updateValFromFlash(){
    	visionValidTargetTimeout=getFlashValue("visionValidTargetTimeout",0.25);
    }
   
	private double getFlashValue(String key, double default_value){
		if(!prefs.containsKey(key)) prefs.putDouble(key, default_value);
		return prefs.getDouble(key, default_value);		
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
//		setDefaultCommand(new UpdateVisionBoiler());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
    public double getDistanceTargetBoiler() {
		return this.getDistanceFromInterpolation();
	}

	public double getAngleTargetHorizontalBoiler() {
		int px = (int) networkTable.getNumber("boiler_angle_cy", -1);
		
		if (px < 0) {
			return -9999.0;
		}
		
		double degPxRatio = 1 / 12.30355;
		
		double angle = degPxRatio * (180 - px);
		// if the camera is rotated the other way
		// double angle = degPxRatio * (px - 180)
		return angle;
	}
	
	public void enable(){
		networkTable.putBoolean("enabled", true);
	}
	
	public void disable(){
		networkTable.putBoolean("enabled", false);
	}
	
	public boolean haveValidTargetBoiler(){
		double now = Timer.getFPGATimestamp();
		
		// See how old our robot data is
		// If it's real old, assume it's invalid
		if (now-robotTimestamp<visionValidTargetTimeout){
			return true;
		}else{
			return false;
		}
	}
	public void update(){
		//connect to network tables
		//update our internal values
		//should be run constantly from a defaultcommand
		double time=networkTable.getNumber("boiler_frame_count",0);

		if(visionTimestamp!=time){
			//have new data!
			distanceTarget=networkTable.getNumber("boiler_distance", 0);
			angleTargetHorizontal = networkTable.getNumber("boiler_angle",0);
			visionTimestamp = time;
			robotTimestamp=Timer.getFPGATimestamp();

		}
	}
	
	public double getDistanceFromInterpolation() {
		double[][] table = {
			// {px, distance(in)}
			{96  + 23.0/2, 	159},
			{100 + 24.0/2, 	150},
			{110 + 25.0/2, 	144},
			{121 + 24.0/2, 	138},
			{130 + 25.0/2, 	132},
			{142 + 27.0/2, 	126},
			{153 + 28.0/2, 	120},
			{167 + 28.0/2,	114},
			{180 + 29.0/2,	108},
			{198 + 30.0/2,	102},
			{219 + 32.0/2,	96},
			{238 + 32.0/2,	90},
			{259 + 33.0/2,	84},
			{280 + 34.0/2,	78},
			{305 + 36.0/2,	72},
			{331 + 36.0/2,	66},
			{359 + 37.0/2,	60},
			{386 + 39.0/2,	54},
			{423 + 40.0/2,	48},
			{464 + 42.0/2,	42},
			{515 + 44.0/2,	36},
			{568 + 42.0/2,	30},
		};
		int px = (int) networkTable.getNumber("boiler_distance_cx", -1);
		
		if (px < 0) {
			return -1.0;
		}
		
		int i = 0;
		// Goes left-to-right, but might need to go RTL if the ratio is inverse
		while (px > table[i][0] && i < table.length - 1)
			i++;
		
		// LTR version
		return table[i][1] + ((table[i+1][1] - table[i][1]) / (table[i+1][0] - table[i][0])) * (px - table[i][0]);
		
		// RTL version
		// return table[i+1][1] + ((table[i][1] - table[i+1][1]) / (table[i][0] - table[i+1][0])) * (px - table[i+1][0]);
	}
	


}

