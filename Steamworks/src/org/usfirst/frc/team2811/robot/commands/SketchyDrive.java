package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SketchyDrive extends Command {

	private double setForward,setRotation;
	
    public SketchyDrive(double forward, double timeout) {
    	requires(Robot.chassis);
    	setForward = forward;
    	setTimeout(timeout);
    }
    
    public SketchyDrive(double forward, double rotation, double timeout) {
    	requires(Robot.chassis);
    	setForward = forward;
    	setTimeout(timeout);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.autoShiftCurrentlyEnabled=false;
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.chassis.drive(setForward,setRotation);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0,0);
    	Robot.chassis.autoShiftCurrentlyEnabled = Robot.chassis.autoShiftDefault;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.drive(0,0);
    	Robot.chassis.autoShiftCurrentlyEnabled = Robot.chassis.autoShiftDefault;
    }
}
