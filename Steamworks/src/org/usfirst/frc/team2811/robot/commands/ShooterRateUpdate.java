package org.usfirst.frc.team2811.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team2811.robot.Robot;

/**
 *
 */
public class ShooterRateUpdate extends Command {
	public ShooterRateUpdate() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.shooter);
	}


	protected void initialize() {
	}

	protected void execute() {
		//Use the joystick to control the shooter rpm, for now
		double targetRate = Robot.shooter.joystickToVelocity(Robot.oi.getShootTargetRate());
		Robot.shooter.setRPM(1500);
		
		//double distance = Robot.vision.getDistance();
		//Robot.shooter.setTargetDistance(distance);
		System.out.println("Current rate: " + Robot.shooter.getCurrentRate());
		System.out.println("Target rate: " + Robot.shooter.joystickToVelocity(targetRate));
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
	
}
