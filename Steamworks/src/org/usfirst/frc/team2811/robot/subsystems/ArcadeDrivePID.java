package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * Expands RobotDrive to allow for MiniPID control of each output
 */
public class ArcadeDrivePID extends RobotDrive {
	
	private CANTalon leftMotor;
	private CANTalon rightMotor;
	
	private MiniPID drivePIDLeft;
	private double leftLowP =  0.0004;
	private double leftLowI =  0.0000;
	private double leftLowD =  0.0000;
	private double leftLowF =  0.0008;
	
	private double leftHighP = 0.00011;
	private double leftHighI = 0.00000;
	private double leftHighD = 0.00000;
	private double leftHighF = 0.00023;

	private MiniPID drivePIDRight;
	private double rightLowP = 0.0004;
	private double rightLowI = 0.0000;
	private double rightLowD = 0.0000;
	private double rightLowF = 0.0008;
	
	private double rightHighP = 0.00011;
	private double rightHighI = 0.00000;
	private double rightHighD = 0.00000;
	private double rightHighF = 0.00023;

	private double 	maxTickRateLow  = 1400;//Tuned for comp bot
	private double 	maxTickRateHigh = 4350; //Tuned for comp bot
	private boolean mapScale;

		
	// Local variables to hold the computed PWM values for the motors
	private double leftMotorSpeed;
	private double rightMotorSpeed;
	
	/**
	 * New class to seamlessly integrate MiniPID functionality into a West-Coast or Tank Drive chassis using CANTalons
	 * @param leftSideLeaderMotor
	 * @param rightSideLeaderMotor
	 */
    public ArcadeDrivePID(CANTalon leftSideLeaderMotor, CANTalon rightSideLeaderMotor){
		
    	super(leftSideLeaderMotor, rightSideLeaderMotor); //Pacifies "safety" things
    	leftMotor = leftSideLeaderMotor;
    	rightMotor = rightSideLeaderMotor;

    	drivePIDLeft = new MiniPID(leftLowP,leftLowI,leftLowD,leftLowF);	
		drivePIDRight = new MiniPID(rightLowP,rightLowI,rightLowD,rightLowF);
		
		//FIXME TEST WITH AUTOSHIFT
		//Voltage per second 
		double ramp = 0.06 ;
		drivePIDLeft.setOutputRampRate(ramp);
		drivePIDRight.setOutputRampRate(ramp);
		
	}
 
    public void setTuningHigh(){
       	drivePIDLeft.setPID(leftHighP, leftHighI, leftHighD, leftHighF);
    	drivePIDRight.setPID(rightHighP, rightHighI, rightHighD, rightHighF);
    	SmartDashboard.putString("Tuning", "HIGH");
    }
    
    public void setTuningLow(){
    	drivePIDLeft.setPID(leftLowP, leftLowI, leftLowD, leftLowF);
    	drivePIDRight.setPID(rightLowP, rightLowI, rightLowD, rightLowF);
    	SmartDashboard.putString("Tuning", "LOW");
    }
    
    public void setMapHigh(){
    	mapScale = true;
    }
    
    public void setMapLow(){
    	mapScale = false;
    }
    
    public double getAbsoluteSpeed(){
    	return Math.abs(leftMotor.getEncVelocity()) + Math.abs(rightMotor.getEncVelocity());
    }
    
	/**
	  * Arcade drive implements two axis driving. This function lets you directly provide
	  * joystick values from any source.
	  *
	  * @param moveValue     The value to use for forwards/backwards
	  * @param rotateValue   The value to use for the rotate right/left
	  */
	public void newArcadeDrive(double moveValue, double rotateValue) {
	    moveValue = limit(moveValue);
	    rotateValue = limit(rotateValue);

	    if (moveValue > 0.0) {
	      if (rotateValue > 0.0) {
	        leftMotorSpeed = moveValue - rotateValue;
	        rightMotorSpeed = Math.max(moveValue, rotateValue);
	      } else {
	        leftMotorSpeed = Math.max(moveValue, -rotateValue);
	        rightMotorSpeed = moveValue + rotateValue;
	      }
	    } else {
	      if (rotateValue > 0.0) {
	        leftMotorSpeed = -Math.max(-moveValue, rotateValue);
	        rightMotorSpeed = moveValue + rotateValue;
	      } else {
	        leftMotorSpeed = moveValue - rotateValue;
	        rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
	      }
	    }
	    
	    newLeftRightDrive(leftMotorSpeed,rightMotorSpeed);
	}
	
	/**
	 * Directly writes values to the PID. Can be used for a tank drive setup, or for more advanced functions.
	 * @param leftMotorSpeed
	 * @param rightMotorSpeed
	 */
	public void newLeftRightDrive(double leftMotorSpeed, double rightMotorSpeed){
		if (!kArcadeStandard_Reported) {
		      HAL.report(tResourceType.kResourceType_RobotDrive, getNumMotors(),
		          tInstances.kRobotDrive_ArcadeStandard);
		      kArcadeStandard_Reported = true;
		}
		
		//PREVENTS WEIRD TINY MOVEMENTS - DON'T TOUCH
		if(leftMotorSpeed<.05&&rightMotorSpeed<.05){
			drivePIDLeft.reset();
			drivePIDRight.reset();
		}
		
		//FIXME Find the correct # and placement of negative signs
		double leftPIDWrite  = drivePIDLeft.getOutput(
				leftMotor.getEncVelocity(),   
				mapToTicks(leftMotorSpeed)*.9);
	    double rightPIDWrite = drivePIDRight.getOutput(
	    		-rightMotor.getEncVelocity(), 
	    		mapToTicks(rightMotorSpeed)*.9);
	    
	    //Use these to force non-PID control 
	    /*
	    double leftPIDWrite  = leftMotorSpeed;
	    double rightPIDWrite = rightMotorSpeed;
	    */
	    
	    leftPIDWrite  = limit(leftPIDWrite);
	    rightPIDWrite = limit(rightPIDWrite);
	    
	    //PRINT SPAM
	    //System.out.println("Output for drive: "+ leftPIDWrite + " | "+ rightPIDWrite);

	    leftMotor.set(leftPIDWrite);
	    rightMotor.set(-rightPIDWrite);
	    
	    //Stops "RobotDrive not updated often enough"
	    if (m_safetyHelper != null) {
		      m_safetyHelper.feed();
		}
	}

	//*****************
	//Utility functions
	//*****************
	
	/** Maps Joystick values to motor ticks for comparing to actual speeds */
    public double mapToTicks(double inputValue){
    	double maxTickRate = mapScale?maxTickRateHigh:maxTickRateLow;
    	double inputMax =  1;
    	double inputMin = -1;
    	double outputMax = maxTickRate;
    	double outputMin = -maxTickRate; 
        return (inputValue/(inputMax-inputMin)-inputMin/(inputMax-inputMin))*(outputMax-outputMin)+outputMin;        
    }
    	
	public void updateValFromFlash(){
		leftLowP = Util.getPreferencesDouble("Chassis leftLowP", 0.0004);
		leftLowI = Util.getPreferencesDouble("Chassis leftLowI", 0.0000);
		leftLowD = Util.getPreferencesDouble("Chassis leftLowD", 0.0000);
		leftLowF = Util.getPreferencesDouble("Chassis leftLowF", 0.0008);
		
		rightLowP = Util.getPreferencesDouble("Chassis rightLowP", 0.0004);
		rightLowI = Util.getPreferencesDouble("Chassis rightLowI", 0.0000);
		rightLowD = Util.getPreferencesDouble("Chassis rightLowD", 0.0000);
		rightLowF = Util.getPreferencesDouble("Chassis rightLowF", 0.0008);
		
		leftHighP = Util.getPreferencesDouble("Chassis leftHighP", 0.00011);
		leftHighI = Util.getPreferencesDouble("Chassis leftHighI", 0.00000);
		leftHighD = Util.getPreferencesDouble("Chassis leftHighD", 0.00000);
		leftHighF = Util.getPreferencesDouble("Chassis leftHighF", 0.00023);
		
		rightHighP = Util.getPreferencesDouble("Chassis rightHighP", 0.00011);
		rightHighI = Util.getPreferencesDouble("Chassis rightHighI", 0.00000);
		rightHighD = Util.getPreferencesDouble("Chassis rightHighD", 0.00000);
		rightHighF = Util.getPreferencesDouble("Chassis rightHighF", 0.00023);

		maxTickRateLow  = Util.getPreferencesDouble("Chassis maxTickRateLow",  1300);  //Tuned for comp bot
		maxTickRateHigh = Util.getPreferencesDouble("Chassis maxTickRateHigh", 4350);  //Tuned for comp bot
	}
}

