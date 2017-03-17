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
	private double speed = 1;
	private Solenoid intakeOpSolenoid; 
	
	public Intake(){
		intakeSolenoid = new Solenoid(0);
		intakeOpSolenoid = new Solenoid(1);
		
		intakeMotor = new CANTalon(3);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		intakeMotor.reverseOutput(true);
		intakeMotor.clearStickyFaults();
		intakeMotor.enable();
		intakeMotor.set(0);
		intakeIn();
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new IntakeBallOff());
    }
    
    
	public boolean isIntakeOn(){
		if(!(intakeMotor.get()!=0)){
			return true;
		}else{
			return false;
		}
	}
/**
 * 
 * @return false when the state is out and returns true when the state in
 */
	public boolean isIntakeIn(){
		return intakeSolenoid.get()==in;
	}
/**
 * This is a function that puts both solenoid in opposite states. This makes it so the actuator pushes the intake out	
 */
	public void intakeOut(){
		intakeSolenoid.set(out);
		intakeOpSolenoid.set(opOut);
	}
/**
 * This is a function that puts both solenoid in opposite states. This makes it so the actuator pushes the intake in	
 */
	public void intakeIn(){
		intakeSolenoid.set(in);
		intakeOpSolenoid.set(opIn);
	}

	/**
	 *  if the current value is too high and the intake is on it returns true. It change the limit of the OutputCurrent
	 *  */

	public boolean isIntakeStalled(){
		if(isIntakeOn() && intakeMotor.getOutputCurrent() > 10){		
			return true;
		}else{
			return false;
		}
		
	}
	
/**
 * Turn on the intake to the set speed. Hardcoded 	
 */
    public void setIntakeOn(){
    	intakeMotor.set(-speed);
    	
    	//TODO find the right value for the motor on
    }
 /**
  * Turns the intake off   
  */
    public void setIntakeOff(){
		intakeMotor.set(0);
    }
    /**
     * Reverse the intake so it spit balls out
     */
    public void reverseIntake(){
    	intakeMotor.set(speed);
    }
}

