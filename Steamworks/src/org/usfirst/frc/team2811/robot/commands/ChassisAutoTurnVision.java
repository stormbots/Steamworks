package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Chassis auto turn command from vision data
 */
public class ChassisAutoTurnVision extends Command {
	
	private double toleranceDegrees;
	private double onePicAngle;
	private boolean haveValidTarget = false;
	
    public ChassisAutoTurnVision(double toleranceDegrees) {
        requires(Robot.chassis);
        requires(Robot.visionGear);
        this.toleranceDegrees = Robot.chassis.getToleranceDegrees();
    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.minipidTurnReset();
    	Robot.chassis.setGearLow();
    	Robot.chassis.setAutoShiftEnabled(false);    
    	Robot.chassis.encoderReset();
//    	if(Robot.visionGear.haveValidTargetGear()){
//    		onePicAngle = -Robot.visionGear.getAngleHorizontal();
//    	}else{
//    		onePicAngle = 0;
//    	}
    	

    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	if (Robot.visionGear.haveValidTargetGear()) {  
//    		double output = Robot.chassis.minipidTurnGetOutput(Robot.chassis.getRotation(), -Robot.visionGear.getAngleHorizontal());
		if(haveValidTarget){    
    	double output = Robot.chassis.minipidTurnGetOutput(
    					Robot.chassis.getRotation(), 
    					onePicAngle
    					);
		Robot.chassis.drive(0, output);
		}else if(Robot.visionGear.haveValidTargetGear()){
			onePicAngle = -Robot.visionGear.getAngleHorizontal();
			haveValidTarget = true;
			Robot.chassis.drive(0, 0);
		}
		
		SmartDashboard.putNumber("Vision Gear Picture Angle", onePicAngle);
    	System.out.println("ChassisAutoTurnVision executing!");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       double rotation = Robot.chassis.getRotation();
//       double target = Robot.visionGear.getAngleHorizontal();
       double target = onePicAngle;
//       SmartDashboard.putNumber("Difference ChassisAutoTurnVision: ", Util.difference(rotation, target));
       return Util.difference(rotation, target) < toleranceDegrees;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.chassis.drive(0, 0);
    	System.out.println("ChassisAutoTurnVision interrupted!");
    }
}
