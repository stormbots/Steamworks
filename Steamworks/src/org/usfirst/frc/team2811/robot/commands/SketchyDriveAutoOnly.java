package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;

/**
 *	Same as Sketchy Drive but with a distance in inches
 */
public class SketchyDriveAutoOnly extends Command {
	
		private double motorPower;
		private double distanceInches;
/**
 * PRACTICE BOT: negative motorSpeed is forward
 * @param motorPower
 * @param inches
 */
    public SketchyDriveAutoOnly(double motorPower, double inches) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.chassis);
    	this.motorPower = motorPower;
    	this.distanceInches = inches;
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.encoderReset();
    	Robot.chassis.setAutoShiftEnabled(false);
    	Robot.chassis.setGearLow();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.chassis.drive(motorPower, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        double actual = Robot.chassis.getFeet()*12.0;
        double target = distanceInches;
        return target - Math.abs(actual) < 0.5;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.drive(0, 0);
    }
}
