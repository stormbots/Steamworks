package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ChassisAutoDrive extends Command {
	
	
	private double targetFeet;
	private double toleranceInches;
	
	
	/** Drive forward or backwards for a number of feet
	 * 
	 * @param feet
	 */
    public ChassisAutoDrive(double feet) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.chassis);
        this.targetFeet = feet;
        this.toleranceInches = Robot.chassis.getToleranceInches();
    }
    
    /**
     * 
     * @param feet
     * @param inches
     */
    public ChassisAutoDrive(double feet, double inches) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.chassis);
        this.targetFeet = feet + (inches/12.0);
        this.toleranceInches = Robot.chassis.getToleranceInches();
    }
    
    

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.autoShiftCurrentlyEnabled = false;
    	Robot.chassis.setGearLow();
    	Robot.chassis.minipidDriveReset();
    	Robot.chassis.encoderReset();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double output = Robot.chassis.minipidDriveGetOutput(Robot.chassis.getFeet(), targetFeet);    	
    	output = - output; // FIXME: THIS SHOULDN'T BE HERE AND WE NEED TO FIX WHY IT IS
    	System.out.println("ChassisAutoDrive executing!");
		Robot.chassis.drive(output, -.2);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Util.difference(Robot.chassis.getFeet()*12.0, targetFeet*12.0) < toleranceInches;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    	Robot.chassis.autoShiftCurrentlyEnabled=Robot.chassis.autoShiftDefault;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.drive(0, 0);

    }
    
    
}
