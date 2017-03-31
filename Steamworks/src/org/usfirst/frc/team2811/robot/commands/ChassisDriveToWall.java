package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.subsystems.MiniPID;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ChassisDriveToWall extends Command {

	private MiniPID leftPID;
	private MiniPID rightPID;
	private double targetInches;
	private double setpointRange = 5.0;
	private double minimumOutputLimit = 0.2;
	
    public ChassisDriveToWall(double targetInches) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.chassis);
         this.targetInches = targetInches;
         
         leftPID=new MiniPID(0,0,0);
         rightPID=new MiniPID(0,0,0);
         //setPOINT RANGE
         //minimumOutputLImit
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.setGearLow();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double outRight = rightPID.getOutput(Robot.gear.distanceRightSideInches(), targetInches);
    	double outLeft = leftPID.getOutput(Robot.gear.distanceLeftSideInches(), targetInches);
    	Robot.chassis.tankDrive(outRight, outLeft);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
