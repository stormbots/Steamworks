package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.ElevatorOff;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Elevator extends Subsystem {

	Preferences prefs = Preferences.getInstance();
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	private CANTalon elevatorMotor;

	private double speed;
	private double power;
	private boolean isCompBot;
	public Elevator(){
		elevatorMotor = new CANTalon(5);
		
        elevatorMotor.reset();
    	elevatorMotor.clearStickyFaults();
    	elevatorMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	//elevatorMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	//Reverse is true on comp bot
    	elevatorMotor.reverseOutput(true);
    	elevatorMotor.enable();
    	elevatorMotor.set(0);
    	
    	//elevatorMotor.setPID(1, 0, 0);
    	updateValFromFlash();
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ElevatorOff());
    }
    
    public void updateValFromFlash(){
    	speed = prefs.getDouble("Elevator Speed", 500);
    	power = prefs.getDouble("Elevator Power", 0.4);
    	isCompBot = prefs.getBoolean("Elevator Output isCompBot", true);
    	if(!prefs.containsKey("Elevator Speed")) prefs.putDouble("Elevator Speed", 500);
    	if(!prefs.containsKey("Elevator Power")) prefs.putDouble("Elevator Power", 0.4);
    	if(!prefs.containsKey("Elevator Output isCompBot")) prefs.putBoolean("Elevator Output isCompBot", true);
    	//We belive that the output is reversed on the comp bot
    	elevatorMotor.reverseOutput(isCompBot);

    }
    /**
     * Changes the control mode to percent v bus and set the power to a variable in pref
     */
    public void setPercentPower(){
    	elevatorMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	elevatorMotor.enable();
    	
    	elevatorMotor.set(power);    	
    }
//    /**
//     * (In the control mode of speed)
//     * Sets the RMP to a value specified in Preferences
//     * NOTE: This system is not tuned (only running on P value)
//     * @param targetRate
//     */
//    public void setRPM(double targetRate){
//    	elevatorMotor.set(targetRate);
//    }
    /**
     * (In the control mode of speed)
     * Sets the RPM to a value specified in Preferences
     * NOTE: This system is not tuned (only running on P value)
     * 
     */
    public void elevatorOn(){
    	elevatorMotor.set(speed);
    }
/**
 * Sets the RPM of the elevator to 0. This mean that the elevator turns off    
 */
    public void elevatorOff(){
    	elevatorMotor.set(0);
    }
    

}

