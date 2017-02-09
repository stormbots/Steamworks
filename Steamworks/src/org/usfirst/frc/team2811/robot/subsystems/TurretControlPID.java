package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * Expands RobotDrive to allow for MiniPID control of each output
 */
public class TurretControlPID extends Subsystem {
	private MiniPID turretPID;
    private double currentOutput;
    private double currentAngle;
    private double targetAngle;
    
    public TurretControlPID(){
    	turretPID = new MiniPID(0.05,0,0);
        turretPID.setOutputLimits(-1, 1);
        turretPID.setDirection(true);
    }

	public void initDefaultCommand() {
		
	}
	
	public void calculateTurretPIDOutput(){
		currentOutput = turretPID.getOutput(currentAngle, targetAngle);
	}
	
	public double getOutput(){
		return currentOutput;
	}
	
	public double getTargetAngle(){
		return targetAngle;
	}
	
	public void setTargetAngle(double angle){
		targetAngle = angle;
	}
	
	public double getCurrentAngle(double angle){
		return currentAngle;
	}
	
	public void setCurrentAngle(double angle){
		currentAngle = angle;
	}
}