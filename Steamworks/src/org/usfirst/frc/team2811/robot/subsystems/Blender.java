package org.usfirst.frc.team2811.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Blender extends Subsystem {

    private CANTalon motor;
    
    
    public Blender(){
    	motor = new CANTalon(4);
    	motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	motor.clearStickyFaults();
	 	motor.enable();
	 	motor.set(0);
    }
    
    public boolean isBlenderOn(){
    	if(!(motor.get()!=0)){
			return true;
		}else{
			return false;
		}
    }
    
    public boolean isBlenderStalled(){
    	//TODO
    	return false;
    }
    
    public void setBlenderOn(){
    	motor.set(0.5);
    }
    
    public void setBlenderOff(){
    	motor.set(0);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

