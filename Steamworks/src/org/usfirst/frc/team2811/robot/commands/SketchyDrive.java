package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SketchyDrive extends Command {

	private double setPower;	
    public SketchyDrive(double power, double timeout) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	setPower = power;
    	setTimeout(timeout);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.chassis.chassisDrive(setPower,0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.chassisDrive(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.chassisDrive(0,0);
    }
}
