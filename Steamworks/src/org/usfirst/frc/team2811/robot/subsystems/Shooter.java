package org.usfirst.frc.team2811.robot.subsystems;

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
	 
	 private double bias = 0.0;
	 
    public Shooter(){
    	shooterMotor = new CANTalon(12);
        shooterMotor.reset();
    	shooterMotor.clearStickyFaults();
    	//Change the motor into speed mode (closed-loop velocity)
    	shooterMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
        shooterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        shooterMotor.reverseSensor(true);
        shooterMotor.reverseOutput(true);
    	shooterMotor.enableBrakeMode(false);
    	shooterMotor.enable();
    	shooterMotor.set(0);
    	
//    	System.out.println("device id: " + shooterMotor.getDeviceID());
//    	shooterMotor2 = new CANTalon(3);
//        shooterMotor2.reset();
//    	shooterMotor2.clearStickyFaults();
//    	//Change the motor into follower mode, mirror the first motor
//    	shooterMotor2.changeControlMode(CANTalon.TalonControlMode.Follower);
//    	shooterMotor2.enableBrakeMode(false);
//    	shooterMotor2.enable();
//    	shooterMotor2.set(shooterMotor.getDeviceID());
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
    	speed = prefs.getDouble("Shooter Speed", 3000);
    	if(!prefs.containsKey("Shooter Speed")) prefs.putDouble("Shooter Speed", 3000);

    	//shooterMotor.set(speed);
    }
    
    public void pidTuneSetRPM(){
    	shooterMotor.set(speed);
    }
    
    //**************************
    // Normal Operation 
    //*************************
    public void setTargetDistance(double distance){
    	targetDistance = distance;
    	setRPM(0);
    }

    public void setRPM(double targetRPM){
    	shooterMotor.set(targetRPM);
    	System.out.println("PID Target Setpoint " + targetRPM);
    	System.out.println("PID Output " + shooterMotor.pidGet());	
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
    
    
    
    //**************************
    // Debug functions 
    //*************************
    
    public double getPIDError(){
       	double error = shooterMotor.getClosedLoopError();
    	System.out.println("Error" + error);
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

