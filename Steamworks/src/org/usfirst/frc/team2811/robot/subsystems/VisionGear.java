package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Util;
import org.usfirst.frc.team2811.robot.commands.UpdateVisionBoiler;
import org.usfirst.frc.team2811.robot.commands.UpdateVisionGear;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionGear extends Subsystem {

	private NetworkTable networkTable;
	private Preferences prefs;
	private double distanceTarget = 0;	
	private double angleTargetHorizontal = 0;
	private double visionTimestamp = 0;
	private double robotTimestamp = 0;
	private boolean isEnabled;
	private double visionValidTargetTimeout;

    public VisionGear() {
    	networkTable=NetworkTable.getTable("vision");
    	prefs = Preferences.getInstance();
    	visionValidTargetTimeout=0;
	}

    public void updateValFromFlash(){
    	visionValidTargetTimeout=Util.getPreferencesDouble("visionValidTargetTimeout",0.1);
    }
   

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
//		setDefaultCommand(new UpdateVisionGear());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	

	public double getAngleHorizontal() {
		return angleTargetHorizontal;
	}
	
	public void enable(){
		networkTable.putBoolean("enabled", true);
	}
	
	public void disable(){
		networkTable.putBoolean("enabled", false);
	}
	
	public boolean haveValidTargetGear(){
		double now = Timer.getFPGATimestamp();
		
		// See how old our robot data is
		// If it's real old, assume it's invalid
		
		if (now-robotTimestamp<visionValidTargetTimeout){
			SmartDashboard.putNumber("TimeStamp", now-robotTimestamp);
			return true;
		}else{
			return false;
		}
		
	}
	public void update(){
		//connect to network tables
		//update our internal values
		//should be run constantly from a defaultcommand
		double time=networkTable.getNumber("gear_frame_number",0);

		if(visionTimestamp!=time){
			//have new data!
			///distanceTarget=networkTable.getNumber("distanceTarget", 0);
			angleTargetHorizontal = networkTable.getNumber("gear_error_angle",0);
			visionTimestamp = time;
			robotTimestamp=Timer.getFPGATimestamp();

		}
	}
	


}

