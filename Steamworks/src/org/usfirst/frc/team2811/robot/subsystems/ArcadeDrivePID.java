package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
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
	private double leftP = .5;
	private double leftI = .005;
	private double leftD = .001;
	private double leftF = 1;
		
	private MiniPID drivePIDRight;
	private double rightP = .5;
	private double rightI = .005;
	private double rightD = .001;
	private double rightF = 1;
	
	private double 	maxTickRate = 4350; //Tuned for comp bot
	private double 	maxIOutput = .1;	//TODO tune later?
	
	/**
	 * New class to seamlessly integrate MiniPID functionality into a West-Coast or Tank Drive chassis using CANTalons
	 * @param leftSideLeaderMotor
	 * @param rightSideLeaderMotor
	 */
    public ArcadeDrivePID(CANTalon leftSideLeaderMotor, CANTalon rightSideLeaderMotor){
		
    	super(leftSideLeaderMotor, rightSideLeaderMotor); //Pacifies "safety" things
    	leftMotor = leftSideLeaderMotor;
    	rightMotor = rightSideLeaderMotor;

    	updateValFromFlash();
    	
    	//drivePIDLeft = new MiniPID(.5,.005,.001,.94);
    	drivePIDLeft = new MiniPID(leftP,leftI,leftD,leftF);
		drivePIDLeft.setOutputLimits(-1,1);
		drivePIDLeft.setMaxIOutput(.1);

		//drivePIDRight = new MiniPID(.5,.005,.001,1);
		drivePIDRight = new MiniPID(rightP,rightI,rightD,rightF);
		drivePIDRight.setOutputLimits(-1,1);
		drivePIDRight.setMaxIOutput(.1);

		//TODO DEBUG REMOVE ME 
    	drivePIDLeft.setPID(0,0,0,.95);
    	drivePIDRight.setPID(0,0,0,1);
	}
    
    
	/**
	  * Arcade drive implements two axis driving. This function lets you directly provide
	  * joystick values from any source.
	  *
	  * @param moveValue     The value to use for forwards/backwards
	  * @param rotateValue   The value to use for the rotate right/left
	  */
	public void newArcadeDrive(double moveValue, double rotateValue) {
	    // Local variables to hold the computed PWM values for the motors
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
		
		if(Robot.chassis.autoShiftCurrentlyEnabled){
			if((Math.abs(leftMotor.getEncVelocity())+Math.abs(rightMotor.getEncVelocity()))>2600){
				Robot.chassis.setGear(true);
			}
			
			if((Math.abs(leftMotor.getEncVelocity())+Math.abs(rightMotor.getEncVelocity()))<2400){
				Robot.chassis.setGear(false);
			}
		}
		
		//PREVENTS WEIRD TINY MOVEMENTS - DON'T TOUCH
		if(leftMotorSpeed<.05&&rightMotorSpeed<.05){
			drivePIDLeft.setMaxIOutput(0);
			drivePIDLeft.reset();
			drivePIDRight.setMaxIOutput(0);
			drivePIDRight.reset();
		} else {
			drivePIDLeft.setMaxIOutput(maxIOutput);
			drivePIDRight.setMaxIOutput(maxIOutput);
		}
		
		//FIXME Find the correct # and placement of negative signs
		double leftPIDWrite  = drivePIDLeft.getOutput(mapToMotorRange(-leftMotor.getEncVelocity()), leftMotorSpeed*.9);
	    double rightPIDWrite = drivePIDRight.getOutput(mapToMotorRange(rightMotor.getEncVelocity()), rightMotorSpeed*.9);
	    
	    //Use these to force non-PID control 
	    /*
	    double leftPIDWrite  = leftMotorSpeed;
	    double rightPIDWrite = rightMotorSpeed;
	    */
	    
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
	
	/** Maps encoder tick value to -1 to 1 for comparing to motor values */
    private double mapToMotorRange(double inputTicks){
    	double maximum =  maxTickRate;
    	double minimum = -maxTickRate;
    	double outputMax = 1;
    	double outputMin = -1; 
        return (inputTicks/(maximum-minimum)-minimum/(maximum-minimum))*(outputMax-outputMin)+outputMin;
         
    }
		
	private void checkKeys(String key, double value){
		if(!prefs.containsKey(key)) prefs.putDouble(key, value);
	}
	
	public void updateValFromFlash(){
		leftP = prefs.getDouble("Chassis leftP", 0.5);
		leftI = prefs.getDouble("Chassis leftI", 0.005);
		leftD = prefs.getDouble("Chassis leftD", 0.001);
		leftF = prefs.getDouble("Chassis leftF", 1);
		
		rightP = prefs.getDouble("Chassis rightP", 0.5);
		rightI = prefs.getDouble("Chassis rightI", 0.005);
		rightD = prefs.getDouble("Chassis rightD", 0.001);
		rightF = prefs.getDouble("Chassis rightF", 1);
		
		maxTickRate = prefs.getDouble("Chassis maxTickRate", 4350);  //Tuned for comp bot
		maxIOutput 	= prefs.getDouble("Chassis maxIOutput",  .1);	//TODO tune later?
		
		checkKeys("Chassis leftP", leftP);
		checkKeys("Chassis leftI", leftI);
		checkKeys("Chassis leftD", leftD);
		checkKeys("Chassis leftF", leftF);
		
		checkKeys("Chassis rightP", rightP);
		checkKeys("Chassis rightI", rightI);
		checkKeys("Chassis rightD", rightD);
		checkKeys("Chassis rightF", rightF);
		
		checkKeys("Chassis maxTickRate", maxTickRate);
		checkKeys("Chassis maxIOutput",  maxIOutput);
	}
}

