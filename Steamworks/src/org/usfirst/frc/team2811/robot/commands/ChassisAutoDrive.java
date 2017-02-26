package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ChassisAutoDrive extends Command {
	
	

	private double targetFeet;
	private double toleranceInches;

	/**
	 * 
	 * @param feet
	 * @param toleranceFeet
	 */
    public ChassisAutoDrive(double feet, double toleranceFeet) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.chassis);
        this.targetFeet = feet;
        this.toleranceInches = toleranceFeet;
    	
    
    }
    
    
    

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.minipidDriveReset();
    	Robot.chassis.drivePIDinit();
    	setTimeout(8);
    	Robot.chassis.autoShiftEnabled = false;
    	Robot.chassis.encoderReset();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double output = Robot.chassis.minipidDriveGetOutput(Robot.chassis.getFeetLeft(), targetFeet);
    	
    	SmartDashboard.putNumber("OutputPIDAutoForward", output);
    	
    	output = - output; // FIXME: THIS SHOULDN'T BE HERE AND WE NEED TO FIX WHY IT IS
    	
		Robot.chassis.drive(output, 0);
    	
		System.out.println("ChassisAutoDrive executing");
		SmartDashboard.putNumber("FeetDriven",Robot.chassis.getFeetLeft());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(isTimedOut())cancel();
        return Util.difference(Robot.chassis.getFeetLeft(), targetFeet) < toleranceInches/12.0;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	SmartDashboard.putString("WARNING:", "Command timed out!");
    }
    
    
}
