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
	
	 public Preferences prefs = Preferences.getInstance();

	 private CANTalon shooterMotor;
	 
	 private int upJoystick = 1;
	 private double upRPM = 5380;
	 
	 private double targetDistance = 0.0;
	 
	 private double speed;
	 private double shooterRPM;
	 
	 private double rpmBias = 0.0;
	 
     private double[] distanceMap = {66, 72, 78, 84, 90};
	 private double[] rpmMap = {3300, 3400, 3450, 3475, 3550};
	 
     //Mapping for the new two-wheel configuration
      private double[][] distanceToRPMMap={
         {4*12,3200},
         {5*12,3250},
         {6*12,3300},
         {7*12,3400},
         {8*12,3525},
         {9*12,3675},
         {10*12,3850},
         {11*12,4100},
      };

	 private double bias = 0.0;
	 
    public Shooter(){
    	//Motor initialization
    	shooterMotor = new CANTalon(12);
        shooterMotor.reset();
    	shooterMotor.clearStickyFaults();
    	//Change the motor into speed mode (closed-loop velocity)
    	shooterMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
        shooterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        
        //TODO MAKE SURE YOU FIX PID IF YOU REVERSE THE MOTOR
        /* 
         * If the shooter wheel starts to run at full speed and refuses to stop even though it is running ShooterOff command, try
         * to reverse the sensor
         */
        //sensor reverse is false on comp bot
        shooterMotor.reverseSensor(false);
       
    	shooterMotor.enableBrakeMode(false);
    	shooterMotor.enableLimitSwitch(false, false);
    	shooterMotor.enable();
    	shooterMotor.set(0);
    	
    	shooterMotor.setProfile(0);
    	//Reverse is true on comp bot
    	shooterMotor.reverseOutput(true);

    	//izone is used to cap the errorSum, 0 disables it
    	//The following line records a pretty consistent PIDF value for wheel currently on comp bot
    	//shooterMotor.setPID(0.05, 0.0, 0.4, 0.028, izone, pidRamprate, pidProfile);
    	
    	//Update preference stuff from SmartDashboard
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
    	
    	//Been tested to work on comp bot so leave it here
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
    }
    
    /**
     * This is for manual control during teleop of a match, using the flapper on the joystick 
     * Top to bottom is 0 to max rpm
     */
    public void manualSetRPM(){
    	setRPM(Robot.oi.getJoystickAngle());
    	//System.out.println("shooter RPM " + Robot.oi.getJoystickAngle());
    }
    
    /**
     * This is for preference set during Auto testing, using the preference called "Shooter Speed" to give a set rpm
     * This is also useful for pid tuning
     * @param targetRPM
     */
    public void setPrefRPM(double targetRPM){
    	setRPM(speed);
    	System.out.println("PID Target Setpoint " + targetRPM);
    	System.out.println("PID Output " + shooterMotor.pidGet());	
    }
    
    /**
     * THis is a hard coded value for Auto shooting, use it when you make sure the rpm works
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
    
    /**
     * This returns the bias so that it can be used in autonomous
     * @return bias
     */
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
    	if(distance < distanceToRPMMap[0][0]) return 0;
    	return Util.getMapValueFromList(distance, distanceToRPMMap) + rpmBias;
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
    		
    		newDistance[i] = Double.parseDouble(pair[0]);
    		newRPM[i] = Double.parseDouble(pair[1]);
    	}
    	
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
    
    public double getPrefRPM(){
    	return speed;
    }
    public double getTargetDistance(){
    	return targetDistance;
    }
    
    /**
     * Returns the close loop error for the shooter wheel 
     * Tips: on SmartDashboard, right click the shooter error box, you can change it to a line plot
     */
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

