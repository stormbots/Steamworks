package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Util;
import org.usfirst.frc.team2811.robot.commands.JoystickDrive;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Preferences;
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

	//Access preference on the SmartDashboard
	Preferences prefs = Preferences.getInstance();
	
    private CANTalon frontLeft;
    private CANTalon topLeft;
    private CANTalon backLeft;
    
    private CANTalon frontRight;
    private CANTalon topRight;
    private CANTalon backRight;

    private ArcadeDrivePID robotDrive;
        
    private Solenoid gearShifter;
    private Solenoid opGearShifter;
    private AHRS navxGyro;
    
    private boolean startingGear;
    public boolean autoShiftEnabled;
    
    
    
//------------------------------------------------------------------------------------------------------------//    
    
	    //**************************************//
	   //                              	       //
	  //    DRIVE COMMAND HELPER FUNCTIONS    //
	 //                                      //
	//**************************************//
    
    /* Practice bot drivefwd map values
		private double ticksForwardLeft = -35005;
		private double ticksForwardRight = 33491;
		private double feetForward = 8.0;
	*/
	
	// Competion bot drivefwd map values
		private double ticksForwardRight; 
		private double ticksForwardLeft;
		private double feetForward;
		
		private MiniPID minipidDrive;
		private double driveP;
		private double driveI;
		private double driveD;
		private double driveMaxI;
		private double driveSetPointRange;
		private double driveMinimumOutputLimit;
		
		public void drivePIDinit(){
			
			ticksForwardRight = Util.getPreferencesDouble("TicksForwardRight", 38170.0);
			ticksForwardLeft = Util.getPreferencesDouble("TicksForwardLeft", -37942.0);
			feetForward = Util.getPreferencesDouble("FeetForward", 10.0);
			
	    	driveP = Util.getPreferencesDouble("DriveFeetPrportional", 0);
	    	driveI = Util.getPreferencesDouble("DriveFeetIntegral", 0);
	    	driveD = Util.getPreferencesDouble("DriveFeetDerivative", 0);
	    	driveMaxI=Util.getPreferencesDouble("DriveFeetMaxI", 0);
	    	driveSetPointRange = Util.getPreferencesDouble("DriveFeetSetpointRange", 0);
	    	driveMinimumOutputLimit = Util.getPreferencesDouble("DriveMinimumOutputLimit", 0.2);
	    	
	    	minipidDrive.setOutputLimits(-1+driveMinimumOutputLimit,1-driveMinimumOutputLimit);
	    	minipidDrive.setSetpointRange(driveSetPointRange);
			minipidDrive.setMaxIOutput(driveMaxI);
			minipidDrive.setPID(driveP, driveI, driveD);
	    }
		
		public void minipidDriveReset(){
			minipidDrive.reset();
		}
		
		public double minipidDriveGetOutput(double actual,double setPoint){
			
			double output = minipidDrive.getOutput(actual, setPoint);
	    	if(output > 0){
	    		output = output + driveMinimumOutputLimit;
	    	}else{
	    		output = output - driveMinimumOutputLimit;
	    	}
			return output;
		}
	
	
	
	
//---------------------------------------------------------------------------------------------------------------//    
	
	    //**************************************//
	   //                              	       //
	  //    TURN COMMAND HELPER FUNCTIONS     //
	 //                                      //
	//**************************************//
	
	/*
	Practice bot map turn values
		private double ticksRotateRight = -29186.0;
		private double degreesForwardRight = 360.0;
	*/
	
	// Competion bot map turn values
		//private double ticksRotateLeft = -282630.0; 
		private double ticksRotateRight;
		private double degreesForwardRight; 
		
		private MiniPID minipidTurn;
		private double turnP;
		private double turnI;
		private double turnD;
		private double turnMaxI;
		private double turnSetPointRange;
		private double turnMinimumOutputLimit;
		
		public void TurnPIDinit(){
			ticksRotateRight = Util.getPreferencesDouble("TicksRotateRight", -287506.0);
			degreesForwardRight = Util.getPreferencesDouble("DeegresForwardRight", 360*10.0);
			
	    	turnP = Util.getPreferencesDouble("TurnProportional", 0);
	    	turnI = Util.getPreferencesDouble("TurnIntegral", 0);
	    	turnD = Util.getPreferencesDouble("TurnDerivative", 0);
	    	turnMaxI=Util.getPreferencesDouble("TurnMaxI", 0);
	    	turnSetPointRange = Util.getPreferencesDouble("TurnSetpointRange", 0);
	    	turnMinimumOutputLimit = Util.getPreferencesDouble("TurnMinimumOutputLimit", 0.2);
	    	
	    	minipidTurn.setOutputLimits(-1+turnMinimumOutputLimit,1-turnMinimumOutputLimit);
	    	minipidTurn.setSetpointRange(turnSetPointRange);
			minipidTurn.setMaxIOutput(turnMaxI);
			minipidTurn.setPID(turnP, turnI, turnD);
		}
		
		public void minipidTurnReset(){
			minipidTurn.reset();
		}
		
		public double minipidTurnGetOutput(double actual, double setPoint){
			return minipidTurn.getOutput(actual, setPoint);
			//TODO: implement detailed getOuput turn behavior
		}
		
	
//---------------------------------------------------------------------------------------------------------------//
	
	
    public Chassis(){
    	frontLeft = new CANTalon(0);
    	topLeft = new CANTalon(1);
    	backLeft = new CANTalon(2);
    	
    	frontRight = new CANTalon(13);
    	topRight = new CANTalon(14);
    	backRight = new CANTalon(15);
    	
    	initTalons();
    	
    	robotDrive = new ArcadeDrivePID(frontLeft,frontRight);   
    	
    	gearShifter = new Solenoid(2);
    	opGearShifter = new Solenoid(3);
    	startingGear = false;
    	autoShiftEnabled = false;
    	setGear(startingGear);
    	
    	minipidDrive = new MiniPID(0,0,0);
    	minipidTurn = new MiniPID(0,0,0);
        
    	navxGyro = new AHRS(SerialPort.Port.kMXP);
    	navxGyro.reset();
    	
        updateValFromFlash();    	
     }
    
    public void initDefaultCommand() {    	    	
    	setDefaultCommand(new JoystickDrive());
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
    	opGearShifter.set(!opGearShifter.get());
    }
    
    public void setGear(boolean gear){
    	gearShifter.set(gear);
    	opGearShifter.set(!gear);
    }
    
    
    //*****************
    //Utility functions
    //*****************
    
    private void checkKeys(String key, boolean value){
		if(!prefs.containsKey(key)) prefs.putBoolean(key, value);
	}
    
    public void updateValFromFlash(){
    	robotDrive.updateValFromFlash();
    	
    	autoShiftEnabled = prefs.getBoolean("Auto Shift", false);
    	startingGear = prefs.getBoolean("Starting Gear", false);
		
		checkKeys("Auto Shift", autoShiftEnabled);
		checkKeys("Starting Gear", startingGear);
	}
    
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


    
    // Encoder 
    
    //TODO put in an utility class
    
    
    public double getRightPosition(){
    	SmartDashboard.putNumber("RightTicks", frontRight.getEncPosition());
		return frontRight.getEncPosition();
	}
	
	public double getLeftPosition(){
		SmartDashboard.putNumber("LeftTicks", frontLeft.getEncPosition());
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
    	return degreesRight;
    }
    
    public void encoderReset(){
		frontRight.setPosition(0);
		frontLeft.setPosition(0);
	}
	    
    //Runs constantly in the background.
    public void updateDashboard(){
    	SmartDashboard.putData("navX-MXP", navxGyro);
    	SmartDashboard.putNumber("Left Encoder", Math.abs(frontLeft.getEncVelocity()));
    	SmartDashboard.putNumber("Right Encoder", Math.abs(frontRight.getEncVelocity()));
    	SmartDashboard.putNumber("Left Encoder (feet)", getFeetLeft());
    	SmartDashboard.putNumber("Right Encoder (feet)", getFeetRight());
    	SmartDashboard.putNumber("Left Write", frontLeft.get());
    	SmartDashboard.putNumber("Right Write", frontRight.get());
    	SmartDashboard.putBoolean("Gear Shifter", gearShifter.get());
    	SmartDashboard.putNumber("Encoder Difference",Math.abs(Math.abs(frontLeft.getEncVelocity())-Math.abs(frontRight.getEncVelocity())));
    	SmartDashboard.putNumber("Encoder Proportion",Math.abs(Math.abs(frontLeft.getEncVelocity())/Math.abs(frontRight.getEncVelocity()+.00001)));
    }
}

