package org.usfirst.frc.team2811.robot.subsystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;
import org.usfirst.frc.team2811.robot.Util;
import org.usfirst.frc.team2811.robot.commands.ShooterOff;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;

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
public class Shooter extends Subsystem{
	 private CANTalon shooterMotor;
	 //private CANTalon shooterMotor2;
	 public Preferences prefs = Preferences.getInstance();
	 private int upJoystick = 1;
	
	 private double upRPM = 5380;
	 
	 private int pidProfile = 0;
	 private double pidRamprate = 0;
	 private int izone = 0;
	 
	 private double targetDistance = 0.0;
	 
	 private double speed;
	 
	 private double[] distanceMap = {0,1};
	 private int[] rpmMap = {3000,4000};
	 
	 private double bias = 0.0;
	 
	 
    public Shooter(){
    	shooterMotor = new CANTalon(12);
        shooterMotor.reset();
    	shooterMotor.clearStickyFaults();
    	//Change the motor into speed mode (closed-loop velocity[]\)
    	shooterMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
        shooterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        shooterMotor.reverseSensor(true);
    	shooterMotor.enableBrakeMode(false);
    	shooterMotor.enableLimitSwitch(false, false);
    	shooterMotor.enable();
    	shooterMotor.set(0);
    	
    	//Reverse is false on comp bot
    	shooterMotor.reverseOutput(false);

    	//izone is used to cap the errorSum, 0 disables it
    	//The following line records a pretty consistent PIDF value
    	//shooterMotor.setPID(0.0tghn  5, 0.0, 0.6, 0.0255, izone, pidRamprate, pidProfile);
    	
    	updateValFromFlash();
    }
    
    public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ShooterOff());
	}
    
    
    public void updateValFromFlash(){
    	speed = Util.getPreferencesDouble("Shooter Speed", 4200);
    	shooterMotor.clearStickyFaults();
    }
//This is for manual control during teleop of a match
    public void pidTuneSetRPM(){
//    	TODO put the speed back in the shooter function so we can edit it manually instead of it being controled by the flap
		//setRPM(speed);
    	setRPM(Robot.oi.getJoystickAngle());
    	//System.out.println("shooter RPM " + Robot.oi.getJoystickAngle());
    }
    
    //**************************
    // Normal Operation 
    //*************************
    public void setTargetDistance(double distance){
    	targetDistance = distance;
    	setRPM(0);
    }
//THis is for preference set during Auto testing
    public void setPrefRPM(double targetRPM){
    	setRPM(speed);
    	System.out.println("PID Target Setpoint " + targetRPM);
    	System.out.println("PID Output " + shooterMotor.pidGet());	
    }
//THis is a hard coded value for Auto shooting
    public void setAutoRPM(double targetRPM){
    	setRPM(targetRPM);
    }
    public void setRPM(double rpm){
    	shooterMotor.set(rpm);
    }
    public double getTargetDistance(){
    	return targetDistance;
    }
    
    public void setBias(double biasAmount){
    	bias = biasAmount;
    }
    
    public double getBias(){
    	return bias;
    }
    
    public void shooterOff(){
    	shooterMotor.set(0);
    }
    
    public double getRPM(double distance){
    	if(distanceMap.length!=rpmMap.length){
    		System.err.println("distanceMap does not match the rpmMap!!!");
    		return rpmMap[0];
    	}
    	for (int i=0; i<distanceMap.length-1;i++){
    		if(distanceMap[i+1]>distance){
    			return map(distance, distanceMap[i], distanceMap[i+1], 
    	    			rpmMap[i], rpmMap[i+1]);
    		}
    	}
    	return rpmMap[distanceMap.length-1];
    }


    //**************************
    // Debug functions 
    //*************************
    
    public double getPIDError(){
       	double error = shooterMotor.getClosedLoopError();
    	//System.out.println("Error" + error);
    	return error;
    }
    
    public double getCurrentRate(){
    	double currentRate = shooterMotor.getSpeed();
    	return currentRate;
    }
    
    public double joystickToVelocity(double input){
    	return map(input, 0, upJoystick, 0, upRPM);
    }
    
    
    
    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
    
    
}

