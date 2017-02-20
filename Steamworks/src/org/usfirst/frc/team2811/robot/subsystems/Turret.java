package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.TurretOff;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Expands RobotDrive to allow for MiniPID control of each output
 */
public class Turret extends Subsystem {

	//Access preference on the SmartDashboard
	Preferences prefs = Preferences.getInstance();
	
	private DigitalInput switchClockwise;
	private DigitalInput switchCounterClockwise;
	
	private CANTalon turretMotor;
	//Save or fetch data into preference
    private int upTicks;
	private int downTicks;
    private int upAngle = 90;
    private int downAngle = 0;
    
    //Only needed for joystick control
    private int downJoystick = -1;
    private int upJoystick = 1;

    private double homingSpeed;
    private boolean homed = false;
    private boolean upTicksSet = false;
    private boolean downTicksSet = false;
       
    private MiniPID turretPID;
    private double P;
    private double I;
    private double D;
    private double currentOutput;
    private double currentAngle;
    private double targetAngle;

	private boolean SWITCH_CLOSED = false;
    
    // **************************
    // Normal operating functions
    // **************************
	public Turret(){
		turretMotor = new CANTalon(6);
        turretMotor.reset();
    	turretMotor.clearStickyFaults();
    	turretMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	turretMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	turretMotor.enable();
    	turretMotor.set(0);
    	

        turretPID = new MiniPID(P,I,D);
        turretPID.setOutputLimits(-1, 1);
        //Reverse is not working -> there's a "-" on CalculatePIDOutput()
        turretPID.setDirection(true);
        updateValFromFlash();
        
        switchClockwise = new DigitalInput(4);
        switchCounterClockwise  = new DigitalInput(5);
      	}
	
	protected void initDefaultCommand() {
		setDefaultCommand(new TurretOff());
	}

	public void updateValFromFlash(){
		upTicks = prefs.getInt("turretUpTicks", -45200);
		downTicks = prefs.getInt("turretDownTicks", -35);
		P = prefs.getDouble("turretP", 1);
		I = prefs.getDouble("turretI", 0);
		D = prefs.getDouble("turretD", 0);
		homingSpeed = prefs.getDouble("turretHomingSpeed", -0.1);
		
		turretPID.setPID(P,I,D);
		
		checkKeys("turretUpTicks", upTicks);
		checkKeys("turretDownTicks", downTicks);
		checkKeys("turretP", P);
		checkKeys("turretI", I);
		checkKeys("turretD", D);
		checkKeys("turretHomingSpeed", homingSpeed);

	}

	//Homing checking limit switch on one side, use the ticks recorded in preference
    public boolean homeCW(){
    	turretMotor.set(-homingSpeed); 
    	if(switchClockwise.get()==SWITCH_CLOSED){
    		turretMotor.setEncPosition(upTicks);
    		homed = true;
    		System.out.println("upTicks: "+upTicks + ", downTicks: "+downTicks);
    	}
    	return homed;
    }
    

    //Homing counterClockwise and set the downTicks
	public boolean homeCCW(){
		//move motor
		turretMotor.set(homingSpeed);
		//look for switch/stall
		if(switchCounterClockwise.get()==SWITCH_CLOSED){
			//set(downTicks)
			downTicks = turretMotor.getEncPosition();
			System.out.println(downTicks);

			downTicksSet = true;
		}
    	return downTicksSet;
    }
    
	//Homing checking limit switches on both sides and record new up/downTicks in preference
	public boolean homeBothWays(){
		//homeCCW until downTicks is set
		if(!downTicksSet){
			homeCCW();
		}else if(!upTicksSet){
			System.out.println("Homed one way");
			//homeCW
			turretMotor.set(-homingSpeed); 
	    	if(switchClockwise.get()==SWITCH_CLOSED){
	    		upTicks = turretMotor.getEncPosition();
				System.out.println(upTicks);
	    		upTicksSet = true;
	    	}
		}
		else if(upTicksSet&&downTicksSet){
			System.out.println("Homed both ways");
    		homed = true;
    		//put into preference
    		prefs.putDouble("turretUpTicks", upTicks);
    		prefs.putDouble("turretDownTicks", downTicks);
    		return true;
    	}
    	return false;
	}
	
	public void setTurretOff(){
		turretMotor.set(0);
	}
    
    //******************
    // PID stuff
    //******************
    public void calculateTurretPIDOutput(){
    	currentAngle = getCurrentAngle();
		currentOutput = - turretPID.getOutput(currentAngle, targetAngle);
		//System.out.println("target: " +targetAngle);
		//System.out.println("current: " +currentAngle);
		
		turretMotor.set(currentOutput);
		//System.out.println("output: " +currentOutput);
		//System.out.println("-------------------------------------");


	}
    
	public void setTargetAngle(double angle){
		targetAngle = angle;
	}
	
	public double getCurrentAngle(){
		currentAngle = ticksToAngle(turretMotor.getEncPosition());
    	return currentAngle;
    }
	

    //*****************
    //Utility functions
    //*****************
	private void checkKeys(String key, double value){
		if(!prefs.containsKey(key)) prefs.putDouble(key, value);
	}
	
	
    public double ticksToAngle(int ticks){
    	return map(ticks, downTicks, upTicks, downAngle, upAngle);
    }
    
    public double joystickToAngle(double input){
    	return map(input, downJoystick, upJoystick, downAngle, upAngle);
    }
    
    private int angleToTicks(double angle){
    	return (int)map(angle, downAngle, upAngle, downTicks, upTicks);
    }
    
    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
    // **************************
    // Debug / Manual functions
    // **************************
    
    public double getOutput(){
		return currentOutput;
	}
    
    public double getTargetAngle(){
		return targetAngle;
	}
    
    public void setTurretMotor(double motorOutput){
    	turretMotor.set(motorOutput);
    }
    
    public boolean isHomed(){
    	return homed;
    }

    public void checkLeftSwitch(){
    	if (!switchCounterClockwise.get()){
    		System.out.println("CounterClockSwitchClosed");
    	}
    }
	
    public void checkRightSwitch(){
    	if (!switchClockwise.get()){
    		System.out.println("ClockSwitchClosed");
    	}
    }
    
}