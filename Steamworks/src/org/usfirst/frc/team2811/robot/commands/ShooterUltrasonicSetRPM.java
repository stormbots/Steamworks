package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This sets the shooter rpm using distance returned by the ultrasonic, can adjust within 1 feet with joystick 
 * axis
 */
public class ShooterUltrasonicSetRPM extends Command {
	
    private double distance;

	public ShooterUltrasonicSetRPM() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	distance = Robot.gear.getDistanceInches()+10;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		Robot.shooter.setTargetDistance(distance+Robot.oi.getShooterDistanceBias());		
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.shooterOff();

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.shooter.shooterOff();
    }
}
