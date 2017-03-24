package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.IntakeBallOff;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

	private CANTalon intakeMotor;
	private Solenoid intakeSolenoid;
	private boolean out = true;
	private boolean in = !out;
	private boolean opOut =!out;
	private boolean opIn = !in;
	private double speed = -0.95;
	private Solenoid intakeOpSolenoid; 
	
	public Intake(){
		intakeSolenoid = new Solenoid(0);
		intakeOpSolenoid = new Solenoid(1);
		
		intakeMotor = new CANTalon(3);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		//true on comp
		intakeMotor.reverseOutput(true);
		intakeMotor.clearStickyFaults();
		intakeMotor.enable();
		intakeMotor.set(0);
		intakeIn();
		
	}
	
	
	public boolean isIntakeOn(){
		if(!(intakeMotor.get()!=0)){
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
		intakeOpSolenoid.set(opOut);
	}
	
	public void intakeIn(){
		intakeSolenoid.set(in);
		intakeOpSolenoid.set(opIn);
	}

	/** if the current value is too high and the intake is on it returns true*/
	// change the limit of the OutputCurrent
	public boolean isIntakeStalled(){
		if(isIntakeOn() && intakeMotor.getOutputCurrent() > 10){		
			return true;
		}else{
			return false;
		}
		
	}
	
	
    public void setIntakeOn(){
    	intakeMotor.set(-speed);
    	
    	//TODO find the right value for the motor on
    }
    
    public void setIntakeOff(){
		intakeMotor.set(0);
    }
    
    public void reverseIntake(){
    	intakeMotor.set(speed);
    }

	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new IntakeBallOff());
    }
    
    
}

