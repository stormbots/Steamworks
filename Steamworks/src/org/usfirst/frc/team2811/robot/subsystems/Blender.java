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
    	motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	motor.clearStickyFaults();
	 	motor.enable();
	 	motor.reverseOutput(true);
	 	
	 	motor.set(0);
	 	
	 	updateValFromFlash();
    }
   
    public void initDefaultCommand() {
       setDefaultCommand(new BlenderOff());
    }
    
    public void updateValFromFlash(){
    	speed = prefs.getDouble("Blender Speed", 0.15);
    	blenderStalled = prefs.getDouble("BlenderStalled", 6000);
    	if(!prefs.containsKey("Blender Speed")) prefs.putDouble("Blender Speed", 0.15);
    	if(!prefs.containsKey("blenderStalled")) prefs.putDouble("blenderStalled", 6000);

    }
    
    public boolean isBlenderOn(){
    	if(!(motor.get()!=0)){
			return true;
		}else{
			return false;
		}
    }
    	
    public boolean isBlenderStalled(){
    	System.out.println("BlenderCurrent " + motor.getOutputCurrent());
    	if(motor.getOutputCurrent() > 6000) return true;
    	return false;
    }
    
    public void setBlenderOn(){
    	motor.set(speed);
    }
    
    public void setBlenderOff(){
    	motor.set(0);
    }
    public void setBlenderReverse(){
    	motor.set(-0.05);
    }
    

}

