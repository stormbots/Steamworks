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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * Expands RobotDrive to allow for MiniPID control of each output
 */
public class Shooter extends Subsystem{
	 private CANTalon shooterMotor;
	 public Preferences prefs = Preferences.getInstance();
	 private int upJoystick = 1;
	
	 private double upRPM = 5380;
	 
	 private int pidProfile = 0;
	 private double pidRamprate = 0;
	 private int izone = 0;
	 
	 private double targetDistance = 0.0;
	 
	 private double speed;
	 private double shooterRPM;
	 
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
        
        //TODO MAKE SURE YOU FIX PID IF YOU REVERSE THE MOTOR
        //sensor reverse is true on comp bot
        shooterMotor.reverseSensor(true);
    	shooterMotor.enableBrakeMode(false);
    	shooterMotor.enableLimitSwitch(false, false);
    	shooterMotor.enable();
    	shooterMotor.set(0);
    	
    	shooterMotor.setProfile(0);
    	//Reverse is true on comp bot, maybe false
    	shooterMotor.reverseOutput(true);

    	//New wheel pid P=0.07, I=5E-06, D=0.4, F-f=0.028

    	//izone is used to cap the errorSum, 0 disables it
    	//The following line records a pretty consistent PIDF value
    	//shooterMotor.setPID(0.05, 0.0, 0.6, 0.0255, izone, pidRamprate, pidProfile);
    	updateValFromFlash();
    }
    public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ShooterOff());
	}
    
    /**
     * Update values needed from the preference
     */
    public void updateValFromFlash(){
    	speed = Util.getPreferencesDouble("Shooter Speed", 4200);
    	shooterMotor.clearStickyFaults();
    	
    	
    	if(!prefs.containsKey("Shooter DistanceToRPMMap")){
    		prefs.putString("Shooter DistanceToRPMMap", "1:100,2:200,3:300,4:400,5:500");
    	}    	
    	
    	//TODO: Run in robot/brain, add or delete another pair of value and see if it runs as expected
    	//If not, fix the function as the comment instructed
    	updateMap("Shooter DistanceToRPMMap", ",", ":");
    	shooterMotor.setInverted(!Util.getPreferencesBoolean("Shooter Output isCompBot", false));
    	//shooterMotor.set(speed);
    }
    
    /**
     * Put values on SmartDashboard for debugging and tuning
     */
    public void updateDashboard(){
    	SmartDashboard.putNumber("Shooter Speed", speed);
    	SmartDashboard.putNumber("Shooter RPM", shooterMotor.get());
    	SmartDashboard.putNumber("Shooter Set Distance", getTargetDistance());
    	SmartDashboard.putNumber("Shooter Distance RPM", getRPM(targetDistance));
    	
    }

    
    //**************************
    // Normal Operation 
    //*************************
    
    /**
     * Set the target distance of the shooter and set shooter rpm according to the distance/rpm map
     * @param distance
     */
    public void setTargetDistance(double distance){
    	targetDistance = distance;
    	setRPM(getRPM(targetDistance));
    	//setRPM(0);
    }
    
    /**
     * This is for manual control during teleop of a match, using the flapper on the joystick
     */
    public void pidTuneSetRPM(){
    	//TODO put the speed back in the shooter function so we can edit it manually instead of it being controled by the flap
		//setRPM(speed);
    	setRPM(Robot.oi.getJoystickAngle());
    	//System.out.println("shooter RPM " + Robot.oi.getJoystickAngle());
    }
    
    /**
     * This is for preference set during Auto testing, using the preference called "Shooter Speed" to give a set rpm
     * @param targetRPM
     */
    public void setPrefRPM(double targetRPM){
    	setRPM(speed);
    	System.out.println("PID Target Setpoint " + targetRPM);
    	System.out.println("PID Output " + shooterMotor.pidGet());	
    }
    
    /**
     * THis is a hard coded value for Auto shooting
     * @param targetRPM
     */
    public void setAutoRPM(double targetRPM){
    	setRPM(targetRPM);
    }
    /**
     * This is just a function that puts the shooter at a set RPM
     * @param rpm
     */
    public void setRPM(double rpm){
    	shooterMotor.set(rpm);
    }
    /**
     * This sets a bias that can be used when shooting so it will adjust the set shooter speed by the bias
     * @param biasAmount
     */
    public void setBias(double biasAmount){
    	bias = biasAmount;
    }
    
    public double getBias(){
    	return bias;
    }
    /**
     * This sets the motor speed to zero
     */
    public void shooterOff(){
    	//shooterMotor.reset();
    	shooterMotor.set(0);
    }
    
    /**
     * This function returns the shooter target rpm and resets shooter profile based on the distance and rpm map values, 
     * if the distance is smaller than the closest distance in the list, return 0. If greater than the largest distance, 
     * return the max rpm in the list. 
     * Turn off the elevator and blender if returns 0!!!!!
     * @param distance
     * @return
     */
    public double getRPM(double distance){
    	//setPIDProfile(distance);
    	shooterMotor.setProfile(0);
    	if(distance < distanceMap[0]) return 0;
    	return Util.getMapValueFromLists(distance, distanceMap, rpmMap) + rpmBias;
    }

    /**
     * This sets the shooting rpm bias, used for teleop control, in/decrease a certain amount of rpm to the mapped 
     * value returned
     * @param bias
     */
    public void setShootBias(double bias){
    	rpmBias = bias;
    }
    
    /**
     * Update the distance map and rpm map on preference, using the format of 
     * distance:rpm,distance:rpm,distance:rpm...
     * No space between elements!!!!!
     */
    private void updateMap(String key, String separator1, String separator2){
    	String[] valuePairString = prefs.getString(key, "").split(separator1);
    	double[] newDistance = new double[valuePairString.length];
    	double[] newRPM = new double[valuePairString.length];
    	for(int i = 0; i < valuePairString.length; i++){
    		String[] pair = valuePairString[i].split(separator2);
    		
    		//Put back the next two line if it doesn't work
//    		distanceMap[i] = Double.parseDouble(pair[0]);
//    		rpmMap[i] = Double.parseDouble(pair[1]);
    		
        	//Comment out the next two line if it doesn't work
    		newDistance[i] = Double.parseDouble(pair[0]);
    		newRPM[i] = Double.parseDouble(pair[1]);
    	}
    	
    	//Comment out the next two line if it doesn't work
    	distanceMap = newDistance;
    	rpmMap = newRPM;
    }
    
    /**
     * This function switches between short and long distance (in inches) shooting pid profile depending on the 
     * distance passed in
     * @param distance
     */
    private void setPIDProfile(double distance){
    	if(distance > 84){
    		shooterMotor.setProfile(1);
    	}
    	else{
    		shooterMotor.setProfile(0);
    	}
    }
    
    //**************************
    // Debug functions 
    //*************************
    
    public double getTargetDistance(){
    	return targetDistance;
    }
    
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

