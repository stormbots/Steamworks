package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class VisionBoilerWaitForValidTarget extends Command {

	public VisionBoilerWaitForValidTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.visionBoiler);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.visionBoiler.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.visionBoiler.isValidTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.visionBoiler.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.visionBoiler.disable();
    }
}
