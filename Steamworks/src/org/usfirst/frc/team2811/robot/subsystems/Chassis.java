package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Chassis extends Subsystem {

    private CANTalon frontLeft;
    private CANTalon topLeft;
    private CANTalon backLeft;
    
    private CANTalon frontRight;
    private CANTalon topRight;
    private CANTalon backRight;

    private ArcadeDrivePID robotDrive;
    
    public AHRS navxGyro;  
    
    
    public Chassis(){
    	frontLeft = new CANTalon(0);
    	topLeft = new CANTalon(2);
    	backLeft = new CANTalon(4);
    	
    	frontRight = new CANTalon(1);
    	topRight = new CANTalon(3);
    	backRight = new CANTalon(5);
    	
    	initTalons();
    	
    	robotDrive = new ArcadeDrivePID(frontLeft,frontRight);   
    	navxGyro = new AHRS(SerialPort.Port.kMXP);
    }
    
    public void chassisDrive(double move, double rotate){
    	robotDrive.newArcadeDrive(move, rotate);
    }
    
    public void initTalons(){
    	frontLeft.reset();
    	frontLeft.enable();
    	frontLeft.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	frontLeft.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	frontLeft.clearStickyFaults();
    	
    	topLeft.reset();
    	topLeft.enable();
    	topLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	topLeft.clearStickyFaults();
    	topLeft.set(0);
    	
    	backLeft.reset();
    	backLeft.enable();
    	backLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	backLeft.clearStickyFaults();
    	backLeft.set(0);
    	
    	
    	frontRight.reset();
    	frontRight.enable();
    	frontRight.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	frontRight.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	frontRight.clearStickyFaults();
    	
    	topRight.reset();
    	topRight.enable();
    	topRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	topRight.clearStickyFaults();
    	topRight.set(1);
        
    	backRight.reset();
    	backRight.enable();
    	backRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	backRight.clearStickyFaults();
    	backRight.set(1);
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

