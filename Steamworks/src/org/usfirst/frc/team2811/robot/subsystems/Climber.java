package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.ClimberOff;

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
	private double speed = 1;
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
		
		updateValFromFlash();
	}
	
	public void updateValFromFlash(){
		currentLimit = prefs.getDouble("climberCurrentLimit", 10);
		if(!prefs.containsKey("climberCurrentLimit")){
			prefs.putDouble("climberCurrentLimit", currentLimit);
		}
	}
    public void initDefaultCommand() {
        // Set the default commansd for a subsystem here.
        setDefaultCommand(new ClimberOff());
    }
    
    public void climbUp(){
    		climberMotor.set(-speed);
    }
    public void climbUpSlow(){
    	climberMotor.set(-speed/2);
    }
    
    public double getCurrent(){
    	return climberMotor.getOutputCurrent();
    }
    public void climberOff(){
    	climberMotor.set(0);
    }
    public void climbDown(){
    	climberMotor.set(0);
    }
}

