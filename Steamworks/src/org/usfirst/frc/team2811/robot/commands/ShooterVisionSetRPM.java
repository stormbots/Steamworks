package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Use the distance returned from the vision to determine the rpm needed, can be adjusted using joystick axis
 */
public class ShooterVisionSetRPM extends Command {
	
    public ShooterVisionSetRPM() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	Robot.shooter.setShootBias(Robot.oi.getShooterRPMBias());
//    	Robot.shooter.setRPM(Robot.shooter.getRPM(distance));
    	if(Robot.visionBoiler.isValidTarget()){
    		double distance = Robot.visionBoiler.getDistanceTargetBoiler();
    		Robot.shooter.setTargetDistance(distance+Robot.oi.getShooterRPMBias());		
    	}
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
