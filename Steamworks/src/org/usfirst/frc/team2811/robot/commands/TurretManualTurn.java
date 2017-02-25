package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurretManualTurn extends Command {

    public TurretManualTurn() {
    	requires(Robot.turret);
       
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.oi.isTurningClock()){
        	Robot.turret.setTurretMotor(0.1);
        	//Robot.turret.checkLeftSwitch();
    	}else if(Robot.oi.isTurningCounterClock()){
    		Robot.turret.setTurretMotor(-0.1);
    	}else{
    		Robot.turret.setTurretMotor(0);
    	}
    	//Robot.turret.checkLeftSwitch();
    	//Robot.turret.checkRightSwitch();
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
