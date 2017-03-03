package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.subsystems.Chassis;
import org.usfirst.frc.team2811.robot.subsystems.MiniPID;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToHeading extends Command {

	private double targetHeading;
	MiniPID headingPID;
	
    public TurnToHeading(double heading) {
    	requires(Robot.chassis);
        targetHeading = heading;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	headingPID = new MiniPID(.9/30, 0, 0);
    	headingPID.setOutputLimits(-1,1);
    	headingPID.setSetpointRange(30);
    	headingPID.setMaxIOutput(.1);
    	
    	//Robot.chassis.resetGyro();    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Robot.chassis.drive(0, headingPID.getOutput(Robot.chassis.getYaw(), targetHeading));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished(){
    	//return Math.abs(Robot.chassis.getYaw()-targetHeading)<2;
    	return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.drive(0, 0);
    }
}
