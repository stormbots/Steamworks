package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurretSetTargetAngleFromVision extends Command {
	
    public TurretSetTargetAngleFromVision() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.vision.enable();
    	//setTimeout(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double target;
    	if(Robot.vision.haveValidTarget()){
    		target = Robot.turret.getTargetAngle()-Robot.vision.getAngleTargetHorizontal();
    		Robot.turret.setTargetAngle(target);
    		//setTimeout(1);
    	}
    	Robot.turret.calculateTurretPIDOutput();
    	Robot.turret.setTurretMotor(Robot.turret.getOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//if(isTimedOut())cancel();
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.vision.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.vision.disable();
    }
}
