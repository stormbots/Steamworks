package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickDrive extends Command {

	private double autoShiftHigh,autoShiftLow;
	
    public JoystickDrive() {
        requires(Robot.chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.setAutoShiftEnabled(Robot.chassis.getAutoShiftDefault());
    	
    	autoShiftHigh = Util.getPreferencesDouble("Chassis autoShiftHigh", 2550);
    	autoShiftLow = Util.getPreferencesDouble("Chassis autoShiftLow", 2000);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if(Robot.chassis.isAutoShiftEnabled()){
			if(Robot.chassis.getAbsoluteSpeed()>autoShiftHigh){
				Robot.chassis.setGearHigh();
			}
			
			if(Robot.chassis.getAbsoluteSpeed()<autoShiftLow){
				Robot.chassis.setGearLow();
			}
		}
    	
    	Robot.chassis.drive(Math.abs(Robot.oi.getMoveValue())>.05?Robot.oi.getMoveValue():0, 
    						Math.abs(Robot.oi.getRotateValue())>.05?Robot.oi.getRotateValue():0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.setAutoShiftEnabled(false);
    	Robot.chassis.setGearLow();
    	Robot.chassis.drive(0, 0);
    	
    	System.err.println("Something is borked. Should never happen");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.setAutoShiftEnabled(false);
    	Robot.chassis.setGearLow();
    	Robot.chassis.drive(0, 0);
    	
    	System.err.println("JoystickDrive cancelled. Restart to drive.");
    }
}
