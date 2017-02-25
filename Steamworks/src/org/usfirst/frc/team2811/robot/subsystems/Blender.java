package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.commands.BlenderOff;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Blender extends Subsystem {

	Preferences prefs = Preferences.getInstance();
	
    private CANTalon motor;
    private double speed;
    private double blenderStalled;
    
    
    public Blender(){
    	motor = new CANTalon(4);
    	motor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	//motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        motor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	motor.clearStickyFaults();
	 	motor.enable();
	 	
	 	//Reverse is true on comp bot
	 	motor.reverseOutput(true);
	 	motor.set(0);
	 	updateValFromFlash();
    }
   
    public void initDefaultCommand() {
       setDefaultCommand(new BlenderOff());
    }
    
    public void updateValFromFlash(){
    	speed = prefs.getDouble("Blender Speed", 350);
    	if(!prefs.containsKey("Blender Speed")) prefs.putDouble("Blender Speed", 350);
    }
    
    public boolean isBlenderOn(){
    	if(!(motor.get()!=0)){
			return true;
		}else{
			return false;
		}
    }
    
    public void setBlenderOn(){
    	motor.set(speed);
    }
    
    public void setBlenderOff(){
    	motor.set(0);
    }
    
    public void setBlenderReverse(){
    	motor.set(-speed);
    }
    
    public double getPIDError(){
    	return motor.getClosedLoopError();
    }
}

