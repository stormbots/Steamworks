package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.ShiftGears;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
////////TWO STICK
	public Joystick leftStick;
    public JoystickButton leftTrigger;
    
    public Joystick rightStick;
    public JoystickButton rightTrigger;

////////XBOX    
    public Joystick xBox;
    public JoystickButton x2;
	
////////THREE AXIS    
    public Joystick threeAxis;
    private Button shootButton;
	private Button climbButton;
    private Button clock;
    private Button counterClock;

    
	public OI(){
////////TWO STICK    	
    	leftStick = new Joystick(0);
    	
    	leftTrigger = new JoystickButton(leftStick,1);
    	leftTrigger.whenPressed(new ShiftGears());
    	
    	rightStick = new Joystick(1);
    	
////////XBOX    	
    	xBox = new Joystick(2);
    	
    	x2 = new JoystickButton(xBox,2);
    	x2.whenPressed(new ShiftGears());
    	
////////THREE AXIS    	
		threeAxis = new Joystick(3);
		
		shootButton = new JoystickButton(threeAxis,1);
        shootButton.whenPressed(new ShooterRateUpdate());
        
        clock = new JoystickButton(threeAxis, 3);
        
        counterClock = new JoystickButton(threeAxis, 4);
		
        climbButton = new JoystickButton(threeAxis,10);		
		climbButton.whenPressed(new Climb());

	}
	
	public double getMoveValue(){
    	if(!DriverStation.getInstance().getJoystickName(2).equals("")){
    		return triggerMath();
    	} else {
    		return leftStick.getRawAxis(1);
    	}
    }
    
    public double getRotateValue(){
    	if(!DriverStation.getInstance().getJoystickName(2).equals("")){
    		return xBox.getRawAxis(0);	
    	} else {
    		return rightStick.getRawAxis(0);
    	}
    }

    public double triggerMath(){
    	return Robot.oi.xBox.getRawAxis(2)-Robot.oi.xBox.getRawAxis(3);
    }
    
    public boolean isOperatorControl(){
    	return getMoveValue()>.25||getRotateValue()>.25;
    }
    
    public double getShootTargetRate(){
    	return (threeAxis.getRawAxis(3)+1)/2;
    }
    
    public double getJoystickAngle(){
    	return threeAxis.getRawAxis(3);
    }

    public boolean isTurningClock(){
        return clock.get();
    }
    
    public boolean isTurningCounterClock(){
        return counterClock.get();
    } 

}
