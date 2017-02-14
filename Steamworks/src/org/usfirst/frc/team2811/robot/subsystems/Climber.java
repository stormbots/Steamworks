package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

	private Preferences prefs = Preferences.getInstance();
	private CANTalon climberMotor;
	private CANTalon climberMotor2;
	private double currentLimit;
	
	public Climber(){
		climberMotor = new CANTalon(10);
		climberMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		climberMotor.clearStickyFaults();
		climberMotor.enableBrakeMode(true);
		climberMotor.reset();
		climberMotor.enable();
		climberMotor.set(0);
		
		climberMotor2 = new CANTalon(11);
		climberMotor2.changeControlMode(CANTalon.TalonControlMode.Follower);
		climberMotor2.clearStickyFaults();
		climberMotor2.enableBrakeMode(true);
		climberMotor2.reset();
		climberMotor2.enable();
		climberMotor2.set(climberMotor.getDeviceID());
		
		updateValuesFromFlash();
	}
	
	public void updateValuesFromFlash(){
		currentLimit = prefs.getDouble("climberCurrentLimit", 10);
		if(!prefs.containsKey("climberCurrentLimit")){
			prefs.putDouble("climberCurrentLimit", currentLimit);
		}
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void climbUp(){
    	if(climberMotor.getOutputCurrent()<currentLimit){
    		climberMotor.set(0.5);
    	}
    }
    
    public double getCurrent(){
    	return climberMotor.getOutputCurrent();
    }
}

