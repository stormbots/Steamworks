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
		int[][] table = {
			// {px, distance}
			{1, 2},
			{3, 4},
			{5, 6}
		};
		int px = networkTable.getNumber("boiler_distance_cx", -1);
		
		if (px < 0) {
			return -1.0;
		}
		
		i = 0;
		// Goes left-to-right, but might need to go RTL if the ratio is inverse
		while (px > table[i][0] && i < table.length - 1)
			i++;
		
		// LTR version
		return table[i][1] + ((table[i+1][1] - table[i][1]) / (table[i+1][0] - table[i][0])) * (px - table[i][0]);
		
		// RTL version
		// return table[i+1][1] + ((table[i][1] - table[i+1][1]) / (table[i][0] - table[i+1][0])) * (px - table[i+1][0]);
	}
	


}

