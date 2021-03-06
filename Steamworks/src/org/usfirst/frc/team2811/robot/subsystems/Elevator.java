package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Util;
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
	
	public Elevator(){
		elevatorMotor = new CANTalon(5);
		
        elevatorMotor.reset();
    	elevatorMotor.clearStickyFaults();
    	elevatorMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	elevatorMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	//Reverse is true on comp bot
    	elevatorMotor.reverseOutput(true);
    	elevatorMotor.enable();
    	elevatorMotor.set(0);
    	
    	//Elevator F-f=0.0105
    	//elevatorMotor.setPID(1, 0, 0);
    	updateValFromFlash();
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ElevatorOff());
    }
    /**
     * Update the values needed for preferences into the flash
     */
    public void updateValFromFlash(){
    	speed = prefs.getDouble("Elevator Speed", 500);
    	power = prefs.getDouble("Elevator Power", 0.4);
    	if(!prefs.containsKey("Elevator Speed")) prefs.putDouble("Elevator Speed", 500);
    	if(!prefs.containsKey("Elevator Power")) prefs.putDouble("Elevator Power", 0.4);
    	elevatorMotor.setInverted(Util.getPreferencesBoolean("Elevator Output isCompBot", false));

    }
    /**
     * Changes the mode to percent v bus and set the motor to a set Power that is specified in the smart dashboard preferences
     */
    public void setPercentPower(){
    	elevatorMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	elevatorMotor.enable();
    	
    	elevatorMotor.set(power);    	
    }
    /**
     * This sets the RPM to a rate that is not currently specified and can be used and changes in the specific commands
     * @param targetRate
     */
    public void setRPM(double targetRate){
    	elevatorMotor.set(targetRate);
    }
    /**
     * This sets the elevator RPM to a value that is specified on the smartDashboard
     */
    public void elevatorOn(){
    	elevatorMotor.set(speed);
    }
    /**
     * This sets the elevator RPM to zero
     */
    public void elevatorOff(){
    	elevatorMotor.set(0);
    }
    
    public double getPIDError(){
    	return elevatorMotor.getClosedLoopError();
    }

}

