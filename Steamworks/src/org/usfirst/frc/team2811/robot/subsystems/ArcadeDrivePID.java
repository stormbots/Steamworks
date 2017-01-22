package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * Expands RobotDrive to allow for MiniPID control of each output
 */
public class ArcadeDrivePID extends RobotDrive {

	private CANTalon leftMotor;
	private CANTalon rightMotor;
	
	private MiniPID drivePIDLeft;
	private MiniPID drivePIDRight;

	private double 	maxTickRate;

    public ArcadeDrivePID(CANTalon leftSideMotor, CANTalon rightSideMotor){
		
    	super(leftSideMotor, rightSideMotor);
    	
    	maxTickRate = 2700;
    	
    	leftMotor = leftSideMotor;
    	rightMotor = rightSideMotor;
    	
		drivePIDLeft  =	new MiniPID(.5,.003,0,1);
		drivePIDLeft.setOutputLimits(-1,1);
		drivePIDLeft.setMaxIOutput(.1);
		
		drivePIDRight =	new MiniPID(.5,.003,0,1);
		drivePIDRight.setOutputLimits(-1,1);
		drivePIDRight.setMaxIOutput(.1);
		
	}
    
    /** Maps encoder tick value to a reasonable range for comparing to motor values */
    public double mapToMotorRange(double inputTicks){
    	double maximum = maxTickRate;
    	double minimum = -maxTickRate;
    	double outputMax = 1;
    	double outputMin = -1; 
        return (inputTicks/(maximum-minimum)-minimum/(maximum-minimum))*(outputMax-outputMin)+outputMin;
         
    }
	
	/**
	  * Arcade drive implements single stick driving. This function lets you directly provide
	  * joystick values from any source.
	  *
	  * @param moveValue     The value to use for forwards/backwards
	  * @param rotateValue   The value to use for the rotate right/left
	  */
	public void arcadeDrive(double moveValue, double rotateValue) {
	    // local variables to hold the computed PWM values for the motors
	    if (!kArcadeStandard_Reported) {
	      HAL.report(tResourceType.kResourceType_RobotDrive, getNumMotors(),
	          tInstances.kRobotDrive_ArcadeStandard);
	      kArcadeStandard_Reported = true;
	    }

	    double leftMotorSpeed;
	    double rightMotorSpeed;

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
	    
	    double leftPIDWrite  = drivePIDLeft.getOutput(mapToMotorRange(leftMotor.getEncVelocity()), leftMotorSpeed*.9);
	    double rightPIDWrite = drivePIDRight.getOutput(mapToMotorRange(-rightMotor.getEncVelocity()), rightMotorSpeed*.9);
	    
	    leftMotor.set(Math.abs(leftPIDWrite)<.05?0:leftPIDWrite);
	    rightMotor.set(Math.abs(rightPIDWrite)<.05?0:-rightPIDWrite);
	    
	    if (m_safetyHelper != null) {
		      m_safetyHelper.feed();
		}
	  }
}

