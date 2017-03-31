package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Util;
import org.usfirst.frc.team2811.robot.commands.VisionBoilerUpdate;

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

	private double visionValidTargetTimeout;

    public VisionBoiler() {
    	networkTable=NetworkTable.getTable("vision");
    	prefs = Preferences.getInstance();
    	visionValidTargetTimeout=0;
	}

    public void updateValFromFlash(){
    	visionValidTargetTimeout=Util.getPreferencesDouble("visionValidTargetTimeout",0.25);
    }
   

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new VisionBoilerUpdate());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
    public double getDistanceTargetBoiler() {
		return distanceTarget;
	}
    
	public double getAngleTargetHorizontalBoiler() {
		return angleTargetHorizontal;
	}
	
	public void enable(){
		networkTable.putBoolean("enabled", true);
	}
	
	public void disable(){
		networkTable.putBoolean("enabled", false);
	}
	
	public boolean isValidTarget(){
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

		double boiler_frame=networkTable.getNumber("boiler_distance_cx_frame_id",0);
		SmartDashboard.putNumber("Boiler Vision Heartbeat", boiler_frame);
		
		double time=networkTable.getNumber("boiler_angle_cy_frame_id",0);
		
		if(visionTimestamp!=time){
			//have new data!
			double boiler_pixel_distance=networkTable.getNumber("boiler_distance_cx",0);
			double boiler_pixel_angle=networkTable.getNumber("boiler_angle_cy",0);

			distanceTarget=Util.getMapValueFromList(boiler_pixel_distance, boilerDistanceToInchesMap);
			angleTargetHorizontal = Util.getMapValueFromList(boiler_pixel_angle,boilerAngleToInchesMap);
			visionTimestamp = time;
			robotTimestamp=Timer.getFPGATimestamp();
		}
	}
	
	private double[][] boilerAngleToInchesMap={
		//TODO: This is not fully calibrated, but should work
		//Matches the angle = pixels * 1/12.30355 calculation
		{0.0,	-26},
		{240,	0},
		{480,	26}
	};
	
	private double[][] boilerDistanceToInchesMap={
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
		{568 + 42.0/2,	30}
	};

}

