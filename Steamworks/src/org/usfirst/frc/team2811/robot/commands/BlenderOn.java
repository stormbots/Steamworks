package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This turns on the blender at a certain percent power set by preference
 */
public class BlenderOn extends Command {

    private double last;
    private boolean blenderOn;

	public BlenderOn() {
        requires(Robot.blender);
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

		Robot.blender.setBlenderOn();

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
