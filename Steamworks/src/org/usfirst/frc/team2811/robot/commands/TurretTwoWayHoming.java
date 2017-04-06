package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Home the turret using limit switches on both sides (don't use it, use one way homing!)
 */
public class TurretTwoWayHoming extends Command {

    public TurretTwoWayHoming() {
        requires(Robot.turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.turret.homeBothWays();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Robot.turret.isHomed();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
