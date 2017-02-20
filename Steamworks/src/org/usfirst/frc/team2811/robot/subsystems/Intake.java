package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.IntakeBallOff;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

	private CANTalon motor;
	private Solenoid intakeSolenoid;
	private boolean out = false;
	private boolean in = !out;
	private double speed = 0.95;
	
	public Intake(){
		intakeSolenoid = new Solenoid(0);
		motor = new CANTalon(3);
		motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		motor.clearStickyFaults();
		motor.enable();
		motor.set(0);
		intakeOut();
		
	}
	
	
	public boolean isIntakeOn(){
		if(!(motor.get()!=0)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isIntakeIn(){
		return intakeSolenoid.get()==in;
	}
	
	public void intakeOut(){
		intakeSolenoid.set(out);
	}
	
	public void intakeIn(){
		intakeSolenoid.set(in);
	}

	/** if the current value is too high and the intake is on it returns true*/
	// change the limit of the OutputCurrent
	public boolean isIntakeStalled(){
		if(isIntakeOn() && motor.getOutputCurrent() > 10){		
			return true;
		}else{
			return false;
		}
		
	}
	
	
    public void setIntakeOn(){
    	motor.set(-speed);
    	
    	//TODO find the right value for the motor on
    }
    
    public void setIntakeOff(){
		motor.set(0);
    }
    
    public void reverseIntake(){
    	motor.set(speed);
    }

	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new IntakeBallOff());
    }
    
    
}

