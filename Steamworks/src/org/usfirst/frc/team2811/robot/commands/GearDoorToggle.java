package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearDoorToggle extends Command {

	

	public GearDoorToggle() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	requires(Robot.gear);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		Robot.gear.toggle();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.gear.isClosed() || Robot.gear.isOpen();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
