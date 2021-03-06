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
	
	
	
	private double targetFeet;
	private double targetInches;
	private double toleranceInches;
	
	
    public ChassisDriveUltrasonic(){
    	requires(Robot.chassis);
    	targetInches = Util.getPreferencesDouble("ChassisUltraSonicDistanceInches", 10.1);
    	toleranceInches = Util.getPreferencesDouble("ChassisUltrasonicToleranceInches", 0.2);
    }
    
    public ChassisDriveUltrasonic(double targetFeet,double targetInches, double toleranceInches){
    	requires(Robot.chassis);
    	this.targetFeet = targetFeet;
    	this.targetInches = targetInches;
    	this.toleranceInches = toleranceInches;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.minipidDriveReset();
    	Robot.chassis.setGearLow();
    	Robot.chassis.setAutoShiftEnabled(false);    
    	Robot.chassis.encoderReset();
    	
    }

    // Called repeatedly when this Command is scheduled to run\
    /* The first if statement deal with out of range problems of the ultrasonic, then in the pid
     * the actul is still encoder and the target is inches plus a value from the joystick that gives a range of 
     * +- 2 inches to the driver to change the target
     * (non-Javadoc)
     * @see edu.wpi.first.wpilibj.command.Command#execute()
     */
    protected void execute() {
    	
		if(Robot.gear.getDistanceFeet()!=10.0){
		    double output = Robot.chassis.minipidDriveGetOutput(
	    		Robot.gear.getDistanceFeet(), 
	    		targetInches/12.0 + (Robot.oi.getMoveValue()/12.0)*1);
		    Robot.chassis.drive(output, 0);
		    //System.out.println("ChassisDriveUltrasonic executing!");
		}else{
			Robot.chassis.drive(0, 0);
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	double distance = Math.abs(Robot.gear.getDistanceInches());	
    	SmartDashboard.putNumber("UltrasonicDistanceIsFinished", Robot.gear.getDistanceInches());
    	double target = Math.abs(targetInches);
        return Util.difference(distance,target) < toleranceInches;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.drive(0, 0);
    	System.out.println("ChassisDriveUltrasonic interrupted!");
     }
        
}
