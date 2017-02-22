package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;

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
	private double 	maxIOutput;
		
    public ArcadeDrivePID(CANTalon leftSideMotor, CANTalon rightSideMotor){
		
    	super(leftSideMotor, rightSideMotor);
    	
    	maxTickRate= 4350; //Tuned for comp bot
    	maxIOutput = .1; //May fix later
    	    	
    	leftMotor = leftSideMotor;
    	rightMotor = rightSideMotor;
    	
		//drivePIDLeft = new MiniPID(.75,.005,0,.94);
    	//drivePIDLeft = new MiniPID(.5,.005,.001,1);
    	//drivePIDLeft = new MiniPID(0,0,0,.95);
    	drivePIDLeft = new MiniPID(.5,.005,.001,.94);
		drivePIDLeft.setOutputLimits(-1,1);
		drivePIDLeft.setMaxIOutput(.1);
		
		//drivePIDRight = new MiniPID(.75,.005,0,1);
		//drivePIDRight = new MiniPID(.5,.005,.001,1);
		//drivePIDRight = new MiniPID(0,0,0,1);
		drivePIDRight = new MiniPID(.5,.005,.001,1);
		drivePIDRight.setOutputLimits(-1,1);
		drivePIDRight.setMaxIOutput(.1);
		
	}
    
    /** Maps encoder tick value to a reasonable range for comparing to motor values */
    public double mapToMotorRange(double inputTicks){
    	double maximum =  maxTickRate;
    	double minimum = -maxTickRate;
    	double outputMax = 1;
    	double outputMin = -1; 
        return (inputTicks/(maximum-minimum)-minimum/(maximum-minimum))*(outputMax-outputMin)+outputMin;
         
    }
	
	/**
	  * Arcade drive implements two axis driving. This function lets you directly provide
	  * joystick values from any source.
	  *
	  * @param moveValue     The value to use for forwards/backwards
	  * @param rotateValue   The value to use for the rotate right/left
	  */
	public void newArcadeDrive(double moveValue, double rotateValue) {
	    // local variables to hold the computed PWM values for the motors
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
		
		if(Robot.chassis.autoShiftEnabled){
			if((Math.abs(leftMotor.getEncVelocity())+Math.abs(rightMotor.getEncVelocity()))>2600){
				Robot.chassis.setGear(true);
			}
			
			if((Math.abs(leftMotor.getEncVelocity())+Math.abs(rightMotor.getEncVelocity()))<2400){
				Robot.chassis.setGear(false);
			}
		}
		
		if(leftMotorSpeed<.05&&rightMotorSpeed<.05){
			drivePIDLeft.setMaxIOutput(0);
			drivePIDLeft.reset();
			drivePIDRight.setMaxIOutput(0);
			drivePIDRight.reset();
			
		} else {
			drivePIDLeft.setMaxIOutput(maxIOutput);
			drivePIDRight.setMaxIOutput(maxIOutput);
		}
		
		double leftPIDWrite  = drivePIDLeft.getOutput(mapToMotorRange(leftMotor.getEncVelocity()), leftMotorSpeed*.9);
	    double rightPIDWrite = drivePIDRight.getOutput(mapToMotorRange(-rightMotor.getEncVelocity()), rightMotorSpeed*.9);
	    
	    //Use these to force non-PID control 
	    /*
	    double leftPIDWrite  = leftMotorSpeed;
	    double rightPIDWrite = rightMotorSpeed;
	    */
	    
	    //System.out.println("Output for drive: "+ leftPIDWrite + " | "+ rightPIDWrite);
	    
	    leftMotor.set(leftPIDWrite);
	    rightMotor.set(-rightPIDWrite);
	    
	    if (m_safetyHelper != null) {
		      m_safetyHelper.feed();
		}
	}
}

