package org.usfirst.frc.team2811.robot.subsystems;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ArcadeDriveUniquePID extends RobotDrive {
	
	private MiniPID arcadeDrivePID;
	private double arcadeDriveP;
	private double arcadeDriveI;
	private double arcadeDriveD;
	private double arcadeDriveF;
	private double maxTickRateLow  = 1400;
//	private double maxTickRateHigh = 4350; 
	

	
	
	public ArcadeDriveUniquePID(SpeedController leftMotor, SpeedController rightMotor) {
		super(leftMotor,rightMotor);
		arcadeDrivePID = new MiniPID(arcadeDriveP,arcadeDriveI,arcadeDriveD,arcadeDriveF);
		double ramp = 0.06 ;
		arcadeDrivePID.setOutputRampRate(ramp);
	}
	
	public void joystickDrive(double move, double rotate){
		
		
		
	}
	
	private double drive(double move, double leftTicks, double rightTicks){
		double moveValue = move;
		double left = ticksToDriveRange(leftTicks);
		double right = ticksToDriveRange(rightTicks);
		double output = arcadeDrivePID.getOutput(left, right);
		if(Math.abs(output) > 1.0) output = setInRange(output);
		
		if(moveValue > 0.0){
			moveValue += output;
		}else if(moveValue < 0.0){
			moveValue -= output;
		}else if(moveValue > -0.05 && moveValue < 0.05){
			moveValue = 0.0;
		}
		
		return moveValue;
	}
	
	
	
	
     private double ticksToDriveRange(double inputValue){
    	double maxTickRate = maxTickRateLow;
    	double inputMax =  1;
    	double inputMin = -1;
    	double outputMax = maxTickRate;
    	double outputMin = -maxTickRate; 
        return (inputValue/(inputMax-inputMin)-inputMin/(inputMax-inputMin))*(outputMax-outputMin)+outputMin;        
    }
     
     private double setInRange(double output){
    		 return output = 1.0/output;
     }

	
	
   
}

