package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *  Base chassis class for interfacing with drive-related systems.<br>
 *  Includes controls for drive PID, rotation PID and automatic impact detection.<br>
 *  Initializes CANTalons and MiniPIDs upon instantiation.
 */
public class Chassis extends Subsystem {

    private CANTalon frontLeft;
    private CANTalon topLeft;
    private CANTalon backLeft;
    
    private CANTalon frontRight;
    private CANTalon topRight;
    private CANTalon backRight;

    private ArcadeDrivePID robotDrive;
    private MiniPID headingPID;
    
    private AHRS navxGyro;
    
    private double offset;
	
    public Chassis(){
    	frontLeft = new CANTalon(0);
    	topLeft = new CANTalon(2);
    	backLeft = new CANTalon(4);
    	
    	frontRight = new CANTalon(1);
    	topRight = new CANTalon(3);
    	backRight = new CANTalon(5);
    	
    	robotDrive = new ArcadeDrivePID(frontLeft,frontRight);   

    	navxGyro = new AHRS(SerialPort.Port.kMXP);
    	
    	//TODO Tune PID things
    }
    
    public void initDefaultCommand() {
    	
    	    	
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
    	
    	navxGyro.reset();
    }

    public void drive(double move, double rotate){
    	robotDrive.newArcadeDrive(move, rotate);
    }
        
    /** Must be in range of -179 to 179
     * 
     * @param targetHeading Target angle relative to the robot's orientation  
     * @return PID turn rate for ArcadeDrive
     */
    public double turnToHeading(double targetHeading){    	
    	return headingPID.getOutput(navxGyro.getYaw(),targetHeading);	
    }
    
    public double getYaw(){
    	return navxGyro.getYaw();
    }
    
    //MAKE SURE YOU KNOW WHAT YOU ARE DOING WHEN YOU CALL THIS
    public void resetGyro(){
    	navxGyro.reset();
    }
    
    //Runs constantly in the background.
    public void updateDashboard(){
    	SmartDashboard.putData("navX-MXP", navxGyro);
    }
}

