package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequence;
import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.ClimbDown;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.IntakeBallIn;
import org.usfirst.frc.team2811.robot.commands.IntakeToggle;
import org.usfirst.frc.team2811.robot.commands.ShiftGears;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;
import org.usfirst.frc.team2811.robot.commands.TurretManualControl;
import org.usfirst.frc.team2811.robot.commands.TurretManualTurn;
import org.usfirst.frc.team2811.robot.commands.TurretOneWayHoming;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
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
    public JoystickButton threeAxisButton1;
    public JoystickButton threeAxisButton3;
    public JoystickButton threeAxisButton4;
	public JoystickButton threeAxisButton10;
	private JoystickButton shootButton;
	private JoystickButton shootButton2;
	private JoystickButton climbButton;

    private JoystickButton clock;
    private JoystickButton counterClock;
    private JoystickButton turn;
    
    private JoystickButton intakeInButton;
    private JoystickButton elevatorOnButton;
    private JoystickButton intakeOutButton;
    private JoystickButton climbDownButton;
    private JoystickButton blenderOnButton;
    private JoystickButton intakeOnButton;
    private JoystickButton manualTurretControl;
    private JoystickButton turretCalButton;
    private JoystickButton turretManualControlButton;
    

    
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
    	shootButton.whileHeld(new ShooterSequence());
    	
        manualTurretControl = new JoystickButton(threeAxis,2);
        manualTurretControl.whileHeld(new TurretSetTargetAngle());

        intakeOnButton = new JoystickButton(threeAxis, 3);
        intakeOnButton.whileHeld(new IntakeBallIn());
        
        intakeOutButton = new JoystickButton(threeAxis,4);
        intakeOutButton.whenPressed(new IntakeToggle());
        
        blenderOnButton = new JoystickButton(threeAxis,5);
        blenderOnButton.whileHeld(new BlenderOn());

        climbButton = new JoystickButton(threeAxis,6);
        climbButton.whileHeld(new Climb()); 
        
        climbDownButton = new JoystickButton(threeAxis, 7);
        climbDownButton.whileHeld(new ClimbDown());
        
//      turretCalButton = new JoystickButton(threeAxis,8);
//      turretCalButton.whenPressed(new TurretOneWayHoming());

    	shootButton2 = new JoystickButton(threeAxis, 11);
    	shootButton2.whileHeld(new ShooterTuning());
    	
        elevatorOnButton = new JoystickButton(threeAxis,12);
        elevatorOnButton.whileHeld(new ElevatorOn());
        
        

        //Put back turretCalButton if not manual turn!
        turn = new JoystickButton(threeAxis,8);
        turn.whileHeld(new TurretManualTurn()); 
        clock = new JoystickButton(threeAxis, 9);
        counterClock = new JoystickButton(threeAxis, 10);
        
//		intakeInButton = new JoystickButton(threeAxis,2);
//      intakeInButton.whileHeld(new IntakeIn());
     	
        //shootButton.whileHeld(new ShooterTuning());     

        

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
