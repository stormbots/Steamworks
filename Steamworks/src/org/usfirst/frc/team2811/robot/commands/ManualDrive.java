package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualDrive extends Command {

	private double input;
    public ManualDrive(double input) {
    	requires(Robot.turret);
        this.input = input;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.oi.isTurningClock()){
        	Robot.turret.setTurretMotor(input);
    	}else if(Robot.oi.isTurningCounterClock()){
    		Robot.turret.setTurretMotor(-input);
    	}else{
    		Robot.turret.setTurretMotor(0);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.turret.setTurretMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.turret.setTurretMotor(0);

    }
}
