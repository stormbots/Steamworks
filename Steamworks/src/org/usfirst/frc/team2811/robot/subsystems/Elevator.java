package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Elevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	private CANTalon elevatorMotor;
	private MiniPID elevatorPID;
	
	private double targetRate;
	private double currentRate;
	
	public Elevator(){
		elevatorMotor = new CANTalon(5);
		
        elevatorMotor.reset();
    	elevatorMotor.clearStickyFaults();
    	elevatorMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	elevatorMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	elevatorMotor.enable();
    	elevatorMotor.set(0);
    	
    	elevatorPID = new MiniPID(1,0,0);
    	
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setRPM(double rate){
    	elevatorMotor.set(rate);
    }
    
    public void calculatePIDOutput(){
    	currentRate = elevatorMotor.getSpeed();
    	double output = elevatorPID.getOutput(currentRate, targetRate);
    }
    
    public double getCurrentRate(){
    	currentRate = 0;
    	return elevatorMotor.getSpeed();
    }
}

