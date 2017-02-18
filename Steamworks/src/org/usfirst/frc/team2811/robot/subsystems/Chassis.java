package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
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
        
    private Solenoid gearShifter;
    private AHRS navxGyro;
    
    public boolean autoShiftEnabled;
	
    public Chassis(){
    	frontLeft = new CANTalon(0);
    	topLeft = new CANTalon(1);
    	backLeft = new CANTalon(2);
    	
    	frontRight = new CANTalon(13);
    	topRight = new CANTalon(14);
    	backRight = new CANTalon(15);
    	
    	robotDrive = new ArcadeDrivePID(frontLeft,frontRight);   
    	
    	gearShifter = new Solenoid(1);
    	autoShiftEnabled = true;

    	navxGyro = new AHRS(SerialPort.Port.kMXP);
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
    	topRight.set(13);
        
    	backRight.reset();
    	backRight.enable();
    	backRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	backRight.clearStickyFaults();
    	backRight.set(13);    	
    	
    	
    	navxGyro.reset();
    }

    public void drive(double move, double rotate){
    	robotDrive.newArcadeDrive(move, rotate);
    }
            
    public double getYaw(){
    	return navxGyro.getYaw();
    }
    
    //MAKE SURE YOU KNOW WHAT YOU ARE DOING WHEN YOU CALL THIS
    public void resetGyro(){
    	navxGyro.reset();
    }
    
    public void shiftGears(){
    	gearShifter.set(!gearShifter.get());
    }
    
    public void setGear(boolean gear){
    	gearShifter.set(gear);
    }
    
    //Runs constantly in the background.
    public void updateDashboard(){
    	SmartDashboard.putData("navX-MXP", navxGyro);
    	SmartDashboard.putNumber("Left Encoder", Math.abs(frontLeft.getEncVelocity()));
    	SmartDashboard.putNumber("Right Encoder", Math.abs(frontRight.getEncVelocity()));
    	SmartDashboard.putNumber("Left Write", frontLeft.get());
    	SmartDashboard.putNumber("Right Write", frontRight.get());
    	SmartDashboard.putBoolean("Gear Shifter", gearShifter.get());
    	SmartDashboard.putNumber("Encoder Difference",Math.abs(Math.abs(frontLeft.getEncVelocity())-Math.abs(frontRight.getEncVelocity())));
    	SmartDashboard.putNumber("Encoder Proportion",Math.abs(Math.abs(frontLeft.getEncVelocity())/Math.abs(frontRight.getEncVelocity())));
    }
}

