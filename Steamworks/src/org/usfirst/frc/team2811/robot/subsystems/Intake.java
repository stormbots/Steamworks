package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

	private CANTalon motor;
	private Solenoid intakeSolenoid;
	private boolean out = true;
	private boolean in = !out;

	public Intake(){
		intakeSolenoid = new Solenoid(1);
		motor = new CANTalon(3);
		motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		motor.clearStickyFaults();
		motor.enable();
		motor.set(0);
		 
	}
	
	
	public boolean isIntakeOn(){
		if(!(motor.get()!=0)){
			return true;
		}else{
			return false;
		}
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
    	motor.set(1);
    	
    	//TODO find the right value for the motor on
    }
    
    public void setIntakeOff(){
		motor.set(0);
    }
    
    public void reverseIntake(){
    	motor.set(-1);
    }

	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    
}

