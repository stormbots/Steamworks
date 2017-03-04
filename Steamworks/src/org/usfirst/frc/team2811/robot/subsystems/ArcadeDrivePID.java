package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;

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

	//Access preference on the SmartDashboard
	Preferences prefs = Preferences.getInstance();
	
	private CANTalon leftMotor;
	private CANTalon rightMotor;
	
	private MiniPID drivePIDLeft;
	private double leftLowP =  0.0000;
	private double leftLowI =  0.0000;
	private double leftLowD =  0.0000;
	private double leftLowF =  0.0008;
		
	private double leftHighP = 0.00000;
	private double leftHighI = 0.00000;
	private double leftHighD = 0.00000;
	private double leftHighF = 0.00023;

	private MiniPID drivePIDRight;
	private double rightLowP = 0.0000;
	private double rightLowI = 0.0000;
	private double rightLowD = 0.0000;
	private double rightLowF = 0.0008;

	private double rightHighP = 0.00000;
	private double rightHighI = 0.00000;
	private double rightHighD = 0.00000;
	private double rightHighF = 0.00023;

	private boolean currentGear; 

	private double 	maxTickRateLow  = 1300;//Tuned for comp bot
	private double 	maxTickRateHigh = 4350; //Tuned for comp bot

		
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

    	//updateValFromFlash();
    	
    	//drivePIDLeft = new MiniPID(.5,.005,.001,.94);
    	drivePIDLeft = new MiniPID(leftLowP,leftLowI,leftLowD,leftLowF);
		//drivePIDLeft.setOutputLimits(-1,1);
		
		//drivePIDRight = new MiniPID(.5,.005,.001,1);
		drivePIDRight = new MiniPID(rightLowP,rightLowI,rightLowD,rightLowF);
		//drivePIDRight.setOutputLimits(-1,1);
	}
    
    public void shiftTuning(){
    	currentGear = !currentGear;
    	setTuning(currentGear);
    }
    
    public void setTuning(boolean gear){
    	if(gear==true){
    		drivePIDLeft.setPID(leftHighP, leftHighI, leftHighD, leftHighF);
    		drivePIDRight.setPID(rightHighP, rightHighI, rightHighD, rightHighF);
    	} else {
    		drivePIDLeft.setPID(leftLowP, leftLowI, leftLowD, leftLowF);
    		drivePIDRight.setPID(rightLowP, rightLowI, rightLowD, rightLowF);	
    	}
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
	    SmartDashboard.putNumber("Theoretical left write", leftMotorSpeed);
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
		
		if(Robot.chassis.autoShiftCurrentlyEnabled){
			if((Math.abs(leftMotor.getEncVelocity())+Math.abs(rightMotor.getEncVelocity()))>2600){
				Robot.chassis.setGear(true);
			}
			
			if((Math.abs(leftMotor.getEncVelocity())+Math.abs(rightMotor.getEncVelocity()))<2400){
				Robot.chassis.setGear(false);
			}
		}
		/*
		//PREVENTS WEIRD TINY MOVEMENTS - DON'T TOUCH
		if(leftMotorSpeed<.05&&rightMotorSpeed<.05){
			drivePIDLeft.reset();
			drivePIDRight.reset();
		}
		*/
		//FIXME Find the correct # and placement of negative signs
		double leftPIDWrite  = drivePIDLeft.getOutput( leftMotor.getEncVelocity(),   mapToTicks(leftMotorSpeed)*.9);
	    double rightPIDWrite = drivePIDRight.getOutput(-rightMotor.getEncVelocity(), mapToTicks(rightMotorSpeed)*.9);
	    
	    //Use these to force non-PID control 
	    /*
	    double leftPIDWrite  = leftMotorSpeed;
	    double rightPIDWrite = rightMotorSpeed;
	    */
	    SmartDashboard.putNumber("leftPIDWrite before limit", leftPIDWrite);
	    leftPIDWrite  = limit(leftPIDWrite);
	    rightPIDWrite = limit(rightPIDWrite);
	    
	    //PRINT SPAM
	    //System.out.println("Output for drive: "+ leftPIDWrite + " | "+ rightPIDWrite);
	    SmartDashboard.putNumber("leftPIDWrite", leftPIDWrite);
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
    	double maxTickRate=currentGear?maxTickRateHigh:maxTickRateLow;
    	double inputMax =  1;
    	double inputMin = -1;
    	double outputMax = maxTickRate;
    	double outputMin = -maxTickRate; 
        return (inputValue/(inputMax-inputMin)-inputMin/(inputMax-inputMin))*(outputMax-outputMin)+outputMin;
         
    }
    
    private void checkKeys(String key, double value){
		if(!prefs.containsKey(key)) prefs.putDouble(key, value);
	}
	
	public void updateValFromFlash(){
		leftLowP = prefs.getDouble("Chassis leftLowP", 0.0008);
		leftLowI = prefs.getDouble("Chassis leftLowI", 0.0000);
		leftLowD = prefs.getDouble("Chassis leftLowD", 0.0000);
		leftLowF = prefs.getDouble("Chassis leftLowF", 0.0008);
		
		rightLowP = prefs.getDouble("Chassis rightLowP", 0.0008);
		rightLowI = prefs.getDouble("Chassis rightLowI", 0.0000);
		rightLowD = prefs.getDouble("Chassis rightLowD", 0.0000);
		rightLowF = prefs.getDouble("Chassis rightLowF", 0.0008);
		
		leftHighP = prefs.getDouble("Chassis leftHighP", 0.00023);
		leftHighI = prefs.getDouble("Chassis leftHighI", 0.00000);
		leftHighD = prefs.getDouble("Chassis leftHighD", 0.00000);
		leftHighF = prefs.getDouble("Chassis leftHighF", 0.00023);
		
		rightHighP = prefs.getDouble("Chassis rightHighP", 0.00023);
		rightHighI = prefs.getDouble("Chassis rightHighI", 0.00000);
		rightHighD = prefs.getDouble("Chassis rightHighD", 0.00000);
		rightHighF = prefs.getDouble("Chassis rightHighF", 0.00023);

		maxTickRateLow  = prefs.getDouble("Chassis maxTickRateLow",  1300);  //Tuned for comp bot
		maxTickRateHigh = prefs.getDouble("Chassis maxTickRateHigh", 4350);  //Tuned for comp bot
				
		checkKeys("Chassis leftLowP", leftLowP);
		checkKeys("Chassis leftLowI", leftLowI);
		checkKeys("Chassis leftLowD", leftLowD);
		checkKeys("Chassis leftLowF", leftLowF);
		
		checkKeys("Chassis rightLowP", rightLowP);
		checkKeys("Chassis rightLowI", rightLowI);
		checkKeys("Chassis rightLowD", rightLowD);
		checkKeys("Chassis rightLowF", rightLowF);
		
		checkKeys("Chassis leftHighP", leftHighP);
		checkKeys("Chassis leftHighI", leftHighI);
		checkKeys("Chassis leftHighD", leftHighD);
		checkKeys("Chassis leftHighF", leftHighF);
		
		checkKeys("Chassis rightHighP", rightHighP);
		checkKeys("Chassis rightHighI", rightHighI);
		checkKeys("Chassis rightHighD", rightHighD);
		checkKeys("Chassis rightHighF", rightHighF);
		
		checkKeys("Chassis maxTickRateLow",  maxTickRateLow);
		checkKeys("Chassis maxTickRateHigh", maxTickRateHigh);

	}
}

