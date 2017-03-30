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
	 
	 private double rpmBias = 0.0;
	 
	 //TODO: add values to preference, if the data size matches, before running the code!
	 private double[] distanceMap = {66, 72, 78, 84, 90};
	 private double[] rpmMap = {3300, 3400, 3450, 3475, 3550};
	 
	 private double bias = 0.0;
	 
    public Shooter(){
    	shooterMotor = new CANTalon(12);
        shooterMotor.reset();
    	shooterMotor.clearStickyFaults();
    	//Change the motor into speed mode (closed-loop velocity)
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
    	//shooterMotor.setPID(0.05, 0.0, 0.6, 0.0255, izone, pidRamprate, pidProfile);
    	updateValFromFlash();
    }
    public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ShooterOff());
	}
    
    
    public void updateValFromFlash(){
    	speed = Util.getPreferencesDouble("Shooter Speed", 4200);
    	shooterMotor.clearStickyFaults();
    	
    	if(!prefs.containsKey("Shooter DistanceToRPMMap")){
    		prefs.putString("Shooter DistanceToRPMMap", "1:100,2:200,3:300,4:400,5:500");
    	}    	
    	updateMap("Shooter DistanceToRPMMap", ",", ":");
    	
    }
    //This is for manual control during teleop of a match
    public void pidTuneSetRPM(){
    	//TODO put the speed back in the shooter function so we can edit it manually instead of it being controled by the flap
		//setRPM(speed);
    	setRPM(Robot.oi.getJoystickAngle());
    	//System.out.println("shooter RPM " + Robot.oi.getJoystickAngle());
    }
    private void setPIDProfile(double distance){
    	if(distance > 7){
    		shooterMotor.setProfile(1);
    	}
    	else{
    		shooterMotor.setProfile(0);
    	}
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
    	setPIDProfile(distance);
    	if(distance < distanceMap[0]) return 0;
    	return Util.getMapValueFromLists(distance, distanceMap, rpmMap) + rpmBias;
    }

    public void setShootBias(double bias){
    	rpmBias = bias;
    }
    /*
     * Caution: 
     * 1. Same number of elements in the string and targetMap
     * 2. No space between numbers
     */
    private void updateMap(String key, String separator1, String separator2){
    	String[] valuePairString = prefs.getString(key, "").split(separator1);
    	String[][] valueString = new String[valuePairString.length][2];
    	for(int i = 0; i < valueString.length; i++){
    		String[] pair = valuePairString[i].split(separator2);
    		distanceMap[i] = Double.parseDouble(pair[0]);
    		rpmMap[i] = Double.parseDouble(pair[1]);
    	}
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

