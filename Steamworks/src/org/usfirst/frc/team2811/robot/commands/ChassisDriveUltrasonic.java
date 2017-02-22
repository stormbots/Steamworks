package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.subsystems.MiniPID;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ChassisDriveUltrasonic extends Command {
	
	
	private MiniPID minipid;
	private Preferences prefs;
	private double p;
	private double i;
	private double d;
	private double maxI;
	private double setPointRange;
	private double targetFeet;
	private double targetInches;
	private double driveMinimumOutputLimit;
	private double toleranceInches;
	
	
    public ChassisDriveUltrasonic(double targetFeet,double targetInches, double toleranceInches){
    	requires(Robot.chassis);
    	this.targetFeet = targetFeet;
    	this.targetInches = targetInches;
    	this.toleranceInches = toleranceInches;
    	minipid = new MiniPID(0,0,0);
    	prefs = Preferences.getInstance();
    }
    
    private void initMiniPID(){
    	p = prefs.getDouble("DriveFeetProportional", 0);
    	i = prefs.getDouble("DriveFeetIntegral", 0);
    	d = prefs.getDouble("DriveFeetDerivative", 0);
    	maxI=prefs.getDouble("DriveFeetMaxI", 0);
    	setPointRange = prefs.getDouble("DriveFeetSetpointRange", 0);
    	driveMinimumOutputLimit = prefs.getDouble("DriveMinimumOutputLimit", 0.2);	
    	
    	checkKeys("DriveFeetProportional",p);
    	checkKeys("DriveFeetIntegral",i);
    	checkKeys("DriveFeetDerivative",d);
    	checkKeys("DriveFeetMaxI",maxI);
    	checkKeys("DriveFeetSetpointRange",setPointRange);
    	checkKeys("DriveMinimumOutputLimit", driveMinimumOutputLimit);
    	
    	minipid.setOutputLimits(-1+driveMinimumOutputLimit,1-driveMinimumOutputLimit);
    	minipid.setSetpointRange(setPointRange);
		minipid.setMaxIOutput(maxI);
		minipid.setPID(p, i, d);
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	minipid.reset();
    	initMiniPID();
    	setTimeout(.75);
    	Robot.chassis.autoShiftEnabled = false;
    	Robot.chassis.encoderReset();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	double output = minipid.getOutput(Robot.gear.distanceRightSideInches()/12.0, targetInches/12.0+targetFeet);
    	
    	if(output > 0){
    		output = output + driveMinimumOutputLimit;
    		Robot.chassis.drive(output, 0);
    	}else{
    		output = output - driveMinimumOutputLimit;
    		Robot.chassis.drive(output, 0);
    	}
    	
		SmartDashboard.putNumber("Drive PID Output", output);
    	System.out.println("Ultrasonic drive running!!");


    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

    	//return false;
     	if(isTimedOut())cancel();
    	double distance = Math.abs(Robot.gear.distanceRightSideInches());
    	double target = Math.abs(targetFeet*12.0 + targetInches);
        return Util.difference(distance,target) < toleranceInches;
        		
      
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("Ultrasonic drive exiting!!");
    	Robot.chassis.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	SmartDashboard.putString("WARNING:", "Command timed out!");
    }
    
    //TODO put in utilities
    private void checkKeys(String key, double value){
		if(!prefs.containsKey(key)) prefs.putDouble(key, value);
	}
}
