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
	private double maxTickRateHigh = 4350; 
	

	
	
	public ArcadeDriveUniquePID(SpeedController leftMotor, SpeedController rightMotor) {
		super(leftMotor,rightMotor);
		arcadeDrivePID = new MiniPID(arcadeDriveP,arcadeDriveI,arcadeDriveD,arcadeDriveF);
		double ramp = 0.06 ;
		arcadeDrivePID.setOutputRampRate(ramp);
	}
	

	
   
}

