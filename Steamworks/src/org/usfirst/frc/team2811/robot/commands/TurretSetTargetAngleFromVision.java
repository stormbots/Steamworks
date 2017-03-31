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
    	Robot.visionBoiler.enable();
    	//setTimeout(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double target;
    	if(Robot.visionBoiler.haveValidTargetBoiler()){
		if (Robot.visionBoiler.getAngleTargetHorizontalBoiler() < -180.0) {
			// the method returned -9999, which means the VisionBoiler subsystem
			// couldn't get a value from NT
			return; // we don't want to turn 9999 deg to the left
		}
    		target = Robot.turret.getTargetAngle()-Robot.visionBoiler.getAngleTargetHorizontalBoiler();
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
    	Robot.visionBoiler.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.visionBoiler.disable();
    }
}
