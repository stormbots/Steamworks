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
	
    public ChassisAutoTurn(double targetDegrees) {
        requires(Robot.chassis);
        this.targetDegrees = targetDegrees;
        this.toleranceDegrees = Robot.chassis.getToleranceDegrees();
    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.minipidTurnReset();
    	Robot.chassis.setGearLow();
    	Robot.chassis.setAutoShiftEnabled(false);    
    	Robot.chassis.encoderReset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double output = Robot.chassis.minipidTurnGetOutput(Robot.chassis.getRotation(), targetDegrees);
    	Robot.chassis.drive(0, output);
    	System.out.println("ChassisAutoTurn executing!");
    	SmartDashboard.putNumber("Chassis AutoTurnCommand rotation: " , Robot.chassis.getRotation());
    	SmartDashboard.putNumber("ChassisAutoTurn error", targetDegrees - Robot.chassis.getRotation());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       double rotation = Robot.chassis.getRotation();
       double target = targetDegrees;
       return Util.difference(rotation, target) < toleranceDegrees;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
//    	Robot.chassis.encoderReset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.drive(0, 0);
//    	Robot.chassis.encoderReset();
    	System.out.println("ChassisAutoTurn interrupted!");
    }
}
