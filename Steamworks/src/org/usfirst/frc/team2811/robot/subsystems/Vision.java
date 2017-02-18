package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.UpdateVision;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class Vision extends Subsystem {
	
	private NetworkTable networkTable;
	private double distanceTarget = 0;	
	private double angleTargetHorizontal = 0;
	private double visionTimestamp = 0;
	private double robotTimestamp = 0;

    public Vision() {
    	networkTable=NetworkTable.getTable("ContourData");
	}

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
    public double getDistanceTarget() {
		return distanceTarget;
	}

	public double getAngleTargetHorizontal() {
		return angleTargetHorizontal;
	}
	
	public boolean haveValidTarget(){
		double now = Timer.getFPGATimestamp();
		
		// See how old our robot data is
		// If it's real old, assume it's invalid
		if (now-robotTimestamp<0.25){ //TODO Move to a preference
			return true;
		}else{
			return false;
		}
	}
	public void update(){
		//connect to network tables
		//update our internal values
		//should be run constantly from a defaultcommand
		double time=networkTable.getNumber("timestamp",0);

		if(visionTimestamp!=time){
			//have new data!
			distanceTarget=networkTable.getNumber("distanceTarget", 0);
			angleTargetHorizontal = networkTable.getNumber("angleTargetHorizontal",0);
			visionTimestamp = time;
			robotTimestamp=Timer.getFPGATimestamp();

		}
	}
	
	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new UpdateVision());
    }

}

