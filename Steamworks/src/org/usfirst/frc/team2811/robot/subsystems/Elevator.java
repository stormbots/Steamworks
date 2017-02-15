package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.ElevatorOff;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Elevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	private CANTalon elevatorMotor;

	public Elevator(){
		elevatorMotor = new CANTalon(5);
		
        elevatorMotor.reset();
    	elevatorMotor.clearStickyFaults();
    	elevatorMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	elevatorMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	elevatorMotor.enable();
    	elevatorMotor.set(0);
    	
    	elevatorMotor.setPID(1, 0, 0);
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ElevatorOff());
    }
    
    public void setRPM(double targetRPM){
    	elevatorMotor.set(targetRPM);
    }
    
    public void elevatorOff(){
    	elevatorMotor.set(0);
    }

}

