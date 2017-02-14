package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	private Joystick stick;
	private Button shootButton;
	private Button climbButton;
	
    public OI(){
    	stick = new Joystick(1);

    	shootButton = new JoystickButton(stick,1);
		climbButton = new JoystickButton(stick,10);

    	shootButton.whenPressed(new ShooterRateUpdate());
		climbButton.whenPressed(new Climb());
    }
    
    public double getShootTargetRate(){
    	return (stick.getRawAxis(3)+1)/2;
    }
}
