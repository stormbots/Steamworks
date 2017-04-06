package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;
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
	
	/**
	 *  Drive to "targetInches" away from wall
	 * @param targetInches
	 */
    public ChassisDriveToWall(double targetInches) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.chassis);
         this.targetInches = targetInches;
         
         leftPID=new MiniPID(0,0,0);
         rightPID=new MiniPID(0,0,0);
         
         rightPID.setSetpointRange(setpointRange);
         leftPID.setSetpointRange(setpointRange);
         
         rightPID.setOutputLimits(-1, 1);
         leftPID.setOutputLimits(-1, 1);
         
         
         //minimumOutputLImit
    }
    
    /**
     * Drive until the robot is touching the wall
     */
    public ChassisDriveToWall() {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.chassis);
         this.targetInches = 0;
         
         leftPID=new MiniPID(0,0,0);
         rightPID=new MiniPID(0,0,0);
         
         rightPID.setSetpointRange(setpointRange);
         leftPID.setSetpointRange(setpointRange);
         
         rightPID.setOutputLimits(-1, 1);
         leftPID.setOutputLimits(-1, 1);
         
         
         //minimumOutputLImit
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.setGearLow();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double rightRaw = rightPID.getOutput(Robot.gear.distanceRightSideInches(), targetInches);
    	double leftRaw = leftPID.getOutput(Robot.gear.distanceLeftSideInches(), targetInches);
    	double outRight = Util.pidOutputLimitAdd(rightRaw, minimumOutputLimit);
    	double outLeft = Util.pidOutputLimitAdd(leftRaw, minimumOutputLimit);
    	Robot.chassis.tankDrive(outRight, outLeft);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	double rightUltrasonic = Robot.gear.distanceRightSideInches();
    	double leftUltrasonic = Robot.gear.distanceRightSideInches();
        return Util.difference(rightUltrasonic, leftUltrasonic) < 0.2;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
}
