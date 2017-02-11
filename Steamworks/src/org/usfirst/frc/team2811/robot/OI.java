package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commands.Climb;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	private Joystick stick = new Joystick(1);
	private Button climbButton;
	
	public OI(){
		climbButton = new JoystickButton(stick,10);
		climbButton.whenPressed(new Climb());
	}

}
