package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Chassis auto turn command
 */
public class ChassisAutoTurn extends Command {
	
	private double targetDegrees;
	private double toleranceDegrees;
	
    public ChassisAutoTurn(double targetDegrees, double toleranceDegrees) {
        requires(Robot.chassis);
        this.targetDegrees = targetDegrees;
        this.toleranceDegrees = toleranceDegrees;
    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.minipidTurnReset();
    	Robot.chassis.TurnPIDinit();
    	setTimeout(8);
    	Robot.chassis.autoShiftEnabled = false;
    	Robot.chassis.encoderReset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	double output = Robot.chassis.minipidTurnGetOutput(Robot.chassis.getRotation(), targetDegrees);
//    	Robot.chassis.drive(0, output);
    	
    	// Naive execute 
    	if(!isFinished()){
    		Robot.chassis.drive(0, 0.3);
    	}
    	SmartDashboard.putNumber("ChassiAutoTurnROTATION: " , Robot.chassis.getRotation());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       if(isTimedOut()) cancel();
       double rotation = Robot.chassis.getRotation();
       double target = targetDegrees;
       return Util.difference(rotation, target) < toleranceDegrees;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("ChassisAutoTurn interrupted!");
    }
}
