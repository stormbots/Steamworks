package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commands.TurretManualTurn;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private Joystick stick;
    private Button clock;
    private Button counterClock;

    public OI(){
    	stick = new Joystick(1);
    	
    	clock = new JoystickButton(stick, 3);
    	counterClock = new JoystickButton(stick, 4);
    }
    
    public boolean isTurningClock(){
    	return clock.get();
    }
    
    public boolean isTurningCounterClock(){
    	return counterClock.get();
    }
    
    public double getJoystickAngle(){
    	return stick.getRawAxis(3);
    }

}
