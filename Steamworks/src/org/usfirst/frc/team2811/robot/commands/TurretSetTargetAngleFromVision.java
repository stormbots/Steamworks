package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurretSetTargetAngleFromVision extends Command {
	
	double target;
	
    public TurretSetTargetAngleFromVision() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.visionBoiler.isValidTarget()){
    		target = Robot.turret.getTargetAngle()-Robot.visionBoiler.getAngleTargetHorizontalBoiler();
    		Robot.turret.setTargetAngle(target);
    	}
    	Robot.turret.calculateTurretPIDOutput();
    	Robot.turret.setTurretMotor(Robot.turret.getOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Util.difference(Robot.turret.getTargetAngle(),Robot.turret.getCurrentAngle()) <= 3;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
