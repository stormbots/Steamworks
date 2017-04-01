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
    private int counterClockTicks = 25029;
	private int clockTicks = 13440;
    private int counterClockAngle = 90;
    private int clockAngle = 0;
    
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
    private double motorOutputManual;
    
    

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
    	//turretMotor.reverseOutput(true);
    	//turretMotor.reverseSensor(true);
    	turretMotor.enable();
    	turretMotor.set(0);
    	turretMotor.enableForwardSoftLimit(false);
    	turretMotor.enableReverseSoftLimit(false);
    	
    	
        turretPID = new MiniPID(P,I,D);
        turretPID.setOutputLimits(-0.2, 0.2);
        //Reverse is not working -> there's a "-" on CalculatePIDOutput()
        turretPID.setDirection(true);
        updateValFromFlash();
        
        switchClockwise = new DigitalInput(4);
        switchCounterClockwise  = new DigitalInput(5);
      	}
	
	protected void initDefaultCommand() {
		setDefaultCommand(new TurretOff());
	}
	/**
	 * This makes it so that the homed variable is set false
	 */
	public void reversedHomed(){
		homed = !homed;
	}
	/**
	 * This function is use to update values of the preferences that can be seen on the smart dashboard
	 */
	public void updateValFromFlash(){
		counterClockTicks = prefs.getInt("turretCounterClockTicks", 25029);
		clockTicks = prefs.getInt("turretClockTicks", 13440);
		P = prefs.getDouble("turretP", 0.05);
		I = prefs.getDouble("turretI", 0);
		D = prefs.getDouble("turretD", 0);
		homingSpeed = prefs.getDouble("turretHomingSpeed", -0.1);
		motorOutputManual = prefs.getDouble("TurretManualOutputVal", 0.1);
		
		turretPID.setPID(P,I,D);
		
		checkKeys("turretCounterClockTicks", counterClockTicks);
		checkKeys("turretClockTicks", clockTicks);
		checkKeys("turretP", P);
		checkKeys("turretI", I);
		checkKeys("turretD", D);
		checkKeys("turretHomingSpeed", homingSpeed);
		checkKeys("turretManualOutputVal", motorOutputManual);
	}

	/**
	 * Homing checking limit switch on one side, replace clockticks&counterClockTicks recorded in preference
	 * @return true when the turret is homed, else continue homing
	 */
    public boolean homeCW(){
    	turretMotor.set(-homingSpeed); 
    	if(switchClockwise.get()==SWITCH_CLOSED){
    		turretMotor.setEncPosition(clockTicks);
    		homed = true;
    		System.out.println("upTicks: "+counterClockTicks + ", downTicks: "+clockTicks);
    	}
    	return homed;
    }
    

    /**
     * Homing counterClockwise and set the clockTicks
     */
	public boolean homeCCW(){
		//move motor
		turretMotor.set(homingSpeed);
		//look for switch/stall
		if(switchCounterClockwise.get()==SWITCH_CLOSED){
			//set(downTicks)
			clockTicks = turretMotor.getEncPosition();
			System.out.println(clockTicks);

			downTicksSet = true;
		}
    	return downTicksSet;
    }
    
	/**
	 * Homing checking limit switches on both sides and record new counterClock/clockTicks in preference
	 * @return true if both ticks are set
	 */
	public boolean homeBothWays(){
		//homeCCW until downTicks is set
		if(!downTicksSet){
			homeCCW();
		}else if(!upTicksSet){
			System.out.println("Homed one way");
			//homeCW
			turretMotor.set(-homingSpeed); 
	    	if(switchClockwise.get()==SWITCH_CLOSED){
	    		counterClockTicks = turretMotor.getEncPosition();
				System.out.println(counterClockTicks);
	    		upTicksSet = true;
	    	}
		}
		else if(upTicksSet&&downTicksSet){
			System.out.println("Homed both ways");
    		homed = true;
    		//put into preference
    		prefs.putDouble("turretUpTicks", counterClockTicks);
    		prefs.putDouble("turretDownTicks", clockTicks);
    		return true;
    	}
    	return false;
	}
	/**
	 * Turns the turret off
	 */
	public void setTurretOff(){
		turretMotor.set(0);
	}
    
    //******************
    // PID stuff
    //******************
	/**
	 * Calculate the percent vbus needed given the current angle and target angle
	 */
    public void calculateTurretPIDOutput(){
    	currentAngle = getCurrentAngle();
		currentOutput = turretPID.getOutput(currentAngle, targetAngle);
		//System.out.println("target: " +targetAngle);
		//System.out.println("current: " +currentAngle);
		
		turretMotor.set(currentOutput);
		//System.out.println("output: " +currentOutput);
		//System.out.println("-------------------------------------");


	}
    
    /**
     * Set the target angle for pid to calculate output, have soft limits on either side to prevent turret 
     * turn out of frame and break stuff
     * @param angle
     */
	public void setTargetAngle(double angle){
		if(targetAngle>counterClockAngle) targetAngle = counterClockAngle;
		else if(targetAngle<clockAngle) targetAngle = clockAngle;
		else targetAngle = angle;
	}
	
	/**
	 * Used to update the current angle of the turret, called when calculating the pid output
	 * @return
	 */
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
    	return map(ticks, clockTicks, counterClockTicks, clockAngle, counterClockAngle);
    }
    
    public double joystickToAngle(double input){
    	return map(input, downJoystick, upJoystick, clockAngle, counterClockAngle);
    }
    
    private int angleToTicks(double angle){
    	return (int)map(angle, clockAngle, counterClockAngle, clockTicks, counterClockTicks);
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
    /**
     * sets the motor to a output
     * @param motorOutput
     */
    public void setTurretMotor(double motorOutput){
    	turretMotor.set(motorOutput);
    }
    /**
     * This sets the 
     * @return home = true
     */
    public boolean isHomed(){
    	return homed;
    }
    /**
     * Checks whether the CounterClockwise is closed or open 
     */
    public void checkCounterClockwiseSwitch(){
    	if (!switchCounterClockwise.get()){
    		System.out.println("CounterClockSwitchClosed");
    	}
    }
	/**
	 *  Checks whether the Clockwise is closed or open 
	 */
    public void checkClockwiseSwitch(){
    	if (!switchClockwise.get()){
    		System.out.println("ClockSwitchClosed");
    	}
    }
    
    public double getManualSpeed(){
    	return motorOutputManual;
    }
    /**
     * This returns the current encoder values
     * @return
     */
    public double getCurrentPos(){
    	return turretMotor.getEncPosition();
    }
}