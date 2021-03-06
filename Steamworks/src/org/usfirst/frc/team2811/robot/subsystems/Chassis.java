package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.Util;
import org.usfirst.frc.team2811.robot.commands.JoystickDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *  Base chassis class for interfacing with drive-related systems.<br>
 *  Includes controls for drive PID and rotation PID.<br>
 *  Initializes CANTalons and MiniPIDs upon instantiation.
 */
public class Chassis extends Subsystem {

	//Access preference on the SmartDashboard
	Preferences prefs = Preferences.getInstance();

//BASIC CHASSIS ITEMS ---------------------------------------------------------------------------------------------------------------// 	
    private CANTalon frontLeft;
    private CANTalon topLeft;
    private CANTalon backLeft;
    
    private CANTalon frontRight;
    private CANTalon topRight;
    private CANTalon backRight;

    private ArcadeDrivePID robotDrive;
        
    private Solenoid gearShifter;
    private Solenoid opGearShifter;
    
    private boolean autoShiftEnabled;
    private boolean autoShiftDefault;
    
//DRIVE PID ---------------------------------------------------------------------------------------------------------------//   
	//Comp bot drivefwd map values
	private double ticksForwardLeft;
	private double ticksForwardRight;;
	private double feetForward;
	
	
	private MiniPID minipidDrive;
	private double driveP;
	private double driveI;
	private double driveD;
	private double driveMaxI;
	private double driveSetPointRange;
	private double driveMinimumOutputLimit;
	private double chassisAutoDriveToleranceInches;
	private double driveRampRate;
		
//ROTATION PID ---------------------------------------------------------------------------------------------------------------//
	//Comp bot map turn values
	private double ticksRotateRight;
	private double degreesForwardRight;
	
	
	private MiniPID minipidTurn;
	private double turnP;
	private double turnI;
	private double turnD;
	private double turnMaxI;
	private double turnSetPointRange;
	private double turnMinimumOutputLimit;
	private double toleranceDegrees;
	private double turnRampRate;
//------------------------------------------------------------------------------------------------------------//    
	public Chassis(){
    	frontLeft = new CANTalon(0);
    	topLeft = new CANTalon(1);
    	backLeft = new CANTalon(2);
    	
    	frontRight = new CANTalon(13);
    	topRight = new CANTalon(14);
    	backRight = new CANTalon(15);
    	
    	minipidDrive = new MiniPID(0,0,0);
    	minipidTurn = new MiniPID(0,0,0);
    	
    	gearShifter = new Solenoid(2);
    	opGearShifter = new Solenoid(3);
    	
    	robotDrive = new ArcadeDrivePID(frontLeft,frontRight);
    	robotDrive.updateValFromFlash();
    	
    	initTalons();
    	updateValFromFlash();
    	
    	setGearLow();
     }
	
	public void initDefaultCommand() {
		setDefaultCommand(new JoystickDrive());
    }

//ROBOT DRIVE ---------------------------------------------------------------------------------------------------------------//
    public void drive(double move, double rotate){
    	move = voltageAdjustOutput(move);
    	rotate = voltageAdjustOutput(rotate);
    	robotDrive.newArcadeDrive(move, rotate);
    }
	
    public void shiftGears(){
    	if(gearShifter.get()){
    		setGearLow();
    	} else {
    		setGearHigh();
    	}
    }
    
    public void setGearLow(){
    	gearShifter.set(false);
    	opGearShifter.set(true);
    	robotDrive.setTuningLow();
    	if(autoShiftEnabled){
    		robotDrive.setMapHigh();
    	} else {
    		robotDrive.setMapLow();
    	}
   	}
    
    public void setGearHigh(){
    	gearShifter.set(true);
    	opGearShifter.set(false);
    	robotDrive.setTuningHigh();
    	robotDrive.setMapHigh();
    }
    
    public void setAutoShiftDefault(boolean setDefault){
    	autoShiftDefault=setDefault;
    }
        
    public void setAutoShiftEnabled(boolean autoShift){
    	autoShiftEnabled = autoShift;
   		setGearLow();
    }
    
    public boolean getAutoShiftDefault(){
    	return autoShiftDefault;
    }
    
    public boolean gearState(){
    	return gearShifter.get();
    }
    
    public boolean isAutoShiftEnabled(){
    	return autoShiftEnabled;
    }
    
    public double getAbsoluteSpeed(){
    	return robotDrive.getAbsoluteSpeed();
    }
    
    private double voltageAdjustOutput(double input){
    	double outputMod = Util.map(Robot.PDP.getVoltage(), 6.5, 8.25, 0, 1);
    	outputMod = Util.constrain(outputMod,1,0);
    	
    	if(Robot.PDP.getVoltage()<8.25){
    		DriverStation.getInstance().reportWarning("Software Brownout Protection Triggered",false);
    	}
    	
    	return input * outputMod;
    }
    
//DRIVE PID ---------------------------------------------------------------------------------------------------------------//
	public double minipidDriveGetOutput(double actual,double setPoint){
		
		double output = minipidDrive.getOutput(actual, setPoint);
		
		if(output>-0.01  && output < 0.01){
			output = 0.0;
		}else if(output>0.0){
			output = output + driveMinimumOutputLimit;
		}else if(output < 0.0){
			output = output - driveMinimumOutputLimit;
		}
		
		return output;
	}
	    
	public double getToleranceInches(){
		return chassisAutoDriveToleranceInches;
	}
	
	public void minipidDriveReset(){
		minipidDrive.reset();
	}
		
//TURN PID ---------------------------------------------------------------------------------------------------------------//
	public double minipidTurnGetOutput(double actual, double setPoint){
		double output = minipidTurn.getOutput(actual, setPoint);
		
		if(output>-0.01  && output < 0.01){
			output = 0.0;
		}else if(output>0.0){
			output = output + driveMinimumOutputLimit;
		}else if(output < 0.0){
			output = output - driveMinimumOutputLimit;
		}
		
		return output;
	}
	
	public double getToleranceDegrees(){
		return toleranceDegrees;
	}
	
	public void minipidTurnReset(){
		minipidTurn.reset();
	}
	
//ENCODER FUNCTIONS---------------------------------------------------------------------------------------------------------------//
    public double getRightPosition(){
		return frontRight.getEncPosition();
	}
	
	public double getLeftPosition(){
		return frontLeft.getEncPosition();
	}
	
	public double getFeetLeft(){
    	double ticks=frontLeft.getEncPosition();
        return Util.map(ticks,0,ticksForwardLeft,0,feetForward);
    }
    
    public double getFeetRight(){
    	double ticks = frontRight.getEncPosition();
    	return Util.map(ticks,0,ticksForwardRight,0,feetForward);
	
    }

    public double getFeet(){
    	return (getFeetLeft()+getFeetRight())/2.0;
    }

    public double getRotation(){
    	double degreesRight = Util.map(frontRight.getEncPosition(),0,ticksRotateRight,0,degreesForwardRight);
    	double degreesLeft = Util.map(frontLeft.getEncPosition(),0,ticksRotateRight,0,degreesForwardRight);
    	return (degreesLeft+degreesRight)/2.0;
//    	return degreesRight;
    }
    
    public void encoderReset(){
		frontRight.setPosition(0);
		frontLeft.setPosition(0);
	}
    
    /**
     * 
     * @param right value 
     * @param left value
     */
    public void tankDrive(double right, double left){
    	robotDrive.tankDrive(right, left);
    }
    
    public void setLow5sec(){
    	if(DriverStation.getInstance().getMatchTime() < 5 && DriverStation.getInstance().getMatchTime() > 1 && gearState() == true){
    		setGearLow();
    	}
    }
   
//UTILITY FUNCTIONS ---------------------------------------------------------------------------------------------------------------//
    private void initTalons(){
    	frontLeft.reset();
    	frontLeft.enable();
    	frontLeft.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	frontLeft.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	frontLeft.reverseSensor(false);
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
    }    
        
	private void initDrivePID(){
			
			ticksForwardRight = Util.getPreferencesDouble("TicksForwardRight", 38170.0);
			ticksForwardLeft = Util.getPreferencesDouble("TicksForwardLeft", -37942.0);
			feetForward = Util.getPreferencesDouble("FeetForward", 10.0);
	    	driveP = Util.getPreferencesDouble("DriveFeetProportional", 0);
	    	driveI = Util.getPreferencesDouble("DriveFeetIntegral", 0);
	    	driveD = Util.getPreferencesDouble("DriveFeetDerivative", 0);
	    	driveMaxI=Util.getPreferencesDouble("DriveFeetMaxI", 0);
	    	driveSetPointRange = Util.getPreferencesDouble("DriveFeetSetpointRange", 0);
	    	driveMinimumOutputLimit = Util.getPreferencesDouble("DriveMinimumOutputLimit", 0.2);
	    	chassisAutoDriveToleranceInches = Util.getPreferencesDouble("TOLERANCE (inches)", 1.5);
	    	//from 0 to 1 in 0.25 sec
	    	driveRampRate = Util.getPreferencesDouble("DriveRampRate", 0.0);
	    	
	    	minipidDrive.setOutputLimits(-1+driveMinimumOutputLimit,1-driveMinimumOutputLimit);
	    	minipidDrive.setSetpointRange(driveSetPointRange);
			minipidDrive.setMaxIOutput(driveMaxI);
			minipidDrive.setPID(driveP, driveI, driveD);
			minipidDrive.setOutputRampRate(driveRampRate);
			
	    }
	
    private void initTurnPID(){
    	
    	//DONT DELETE NEVER NEVER NEVER
    	// Ticks: -314866.0
    	// rotations: 3600
    	
		ticksRotateRight = Util.getPreferencesDouble("TicksRotateRight", -314866.0);
		degreesForwardRight = Util.getPreferencesDouble("DeegresForwardRight", 360*10.0);
		toleranceDegrees = Util.getPreferencesDouble("ChassisAutoTurn tolerance degrees", 1);
		
    	turnP = Util.getPreferencesDouble("TurnProportional", 0);
    	turnI = Util.getPreferencesDouble("TurnIntegral", 0);
    	turnD = Util.getPreferencesDouble("TurnDerivative", 0);
    	turnMaxI=Util.getPreferencesDouble("TurnMaxI", 0);
    	turnSetPointRange = Util.getPreferencesDouble("TurnSetpointRange", 0);
    	turnMinimumOutputLimit = Util.getPreferencesDouble("TurnMinimumOutputLimit", 0.2);
    	turnRampRate = Util.getPreferencesDouble("TurnRampRate", 0.0);
    	
    	
    	minipidTurn.setOutputLimits(-1+turnMinimumOutputLimit,1-turnMinimumOutputLimit);
    	minipidTurn.setSetpointRange(turnSetPointRange);
		minipidTurn.setMaxIOutput(turnMaxI);
		minipidTurn.setPID(turnP, turnI, turnD);
		minipidTurn.setOutputRampRate(turnRampRate);
	}
	
    public void updateValFromFlash(){
    	  	
    	autoShiftDefault = Util.getPreferencesBoolean("Chassis Auto Shift", false);
//    	startingGear = Util.getPreferencesBoolean("Chassis Starting Gear", false);
    	
    	robotDrive.updateValFromFlash();
    	initDrivePID();
    	initTurnPID();
	}
    
    //Runs constantly in the background.
    public void updateDashboard(){
//    	SmartDashboard.putNumber("Left Encoder", Math.abs(frontLeft.getEncVelocity()));
//    	SmartDashboard.putNumber("Right Encoder", Math.abs(frontRight.getEncVelocity()));
//    	SmartDashboard.putNumber("Left Encoder", Math.abs(frontLeft.getPosition()));
//    	SmartDashboard.putNumber("Right Encoder", Math.abs(frontRight.getPosition()));
    	SmartDashboard.putNumber("Left Encoder (feet)", getFeetLeft());
    	SmartDashboard.putNumber("Right Encoder (feet)", getFeetRight());
//    	SmartDashboard.putNumber("Left Write", frontLeft.get());
//    	SmartDashboard.putNumber("Right Write", frontRight.get());
//    	SmartDashboard.putBoolean("Gear Shifter", gearShifter.get());
    	SmartDashboard.putNumber("Encoder frontRight Position", frontRight.getEncPosition());
    	SmartDashboard.putNumber("Left Encoder Position", frontLeft.getEncPosition());
//    	SmartDashboard.putNumber("Encoder Difference",Math.abs(Math.abs(frontLeft.getEncVelocity())-Math.abs(frontRight.getEncVelocity())));
//    	SmartDashboard.putNumber("Encoder Proportion",Math.abs(Math.abs(frontLeft.getEncVelocity())/Math.abs(frontRight.getEncVelocity()+.00001)));
    	SmartDashboard.putNumber("Rotation (frontRight)", Math.round(getRotation()));
    }
}
