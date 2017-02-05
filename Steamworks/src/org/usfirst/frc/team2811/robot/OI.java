package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	//JOYSTICKS
	//2stick, xbox, 3axis
	
	//BUTTONS
	//all teh buttons
	
	public OI(){
		
		
		
		
	}
	
	//FIXME Actually declare joystick thingimajigs
	
	public double getMoveValue(){
    	/*if(!DriverStation.getInstance().getJoystickName(2).equals("")){
    		return triggerMath();
    	} else {
    		return leftStick.getRawAxis(1);
    	}*/
		return 0;
    }
    
    public double getRotateValue(){
    	/*if(!DriverStation.getInstance().getJoystickName(2).equals("")){
    		return xBox.getRawAxis(0);	
    	} else {
    		return rightStick.getRawAxis(0);
    	}*/
    	return 0;
    }

    /*public double triggerMath(){
    	return Robot.oi.xBox.getRawAxis(2)-Robot.oi.xBox.getRawAxis(3);
    }*/
	
}
