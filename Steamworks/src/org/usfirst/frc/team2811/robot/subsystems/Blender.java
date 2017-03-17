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
    private boolean isCompBot;
    
    
    public Blender(){
    	motor = new CANTalon(4);
    	//motor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        motor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	motor.clearStickyFaults();
	 	motor.enable();
	 	
	 	motor.reverseOutput(!isCompBot);
	 	motor.set(0);
	 	updateValFromFlash();
    }
   
    public void initDefaultCommand() {
       setDefaultCommand(new BlenderOff());
    }
    
    public void updateValFromFlash(){
    	speed = prefs.getDouble("Blender Speed", 0.5);
    	if(!prefs.containsKey("Blender Speed")) prefs.putDouble("Blender Speed", 0.5);
    	isCompBot = prefs.getBoolean("Blender Output isCompBot", true);
    	if(!prefs.containsKey("Blender Output isCompBot")) prefs.putBoolean("Blender Output isCompBot", true);
    	//We believe that the output is not reversed on the comp bot
	 	motor.reverseOutput(!isCompBot);
    }
    
    public boolean isBlenderOn(){
    	if(!(motor.get()!=0)){
			return true;
		}else{
			return false;
		}
    }
 /**
  * Sets the blender on with power specified in Preferences
  */
    public void setBlenderOn(){
    	motor.set(speed);
    }
 /**
  * Sets the blender power to zero. This turns the blender off   
  */
    public void setBlenderOff(){
    	motor.set(0);
    }
/**
 * Sets the power to reverse of the preference blender speed. This means that it reverses the blender direction    
 */
    public void setBlenderReverse(){
    	motor.set(-speed);
    }
    
    public double getPIDError(){
    	return motor.getClosedLoopError();
    }
}

