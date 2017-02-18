package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequence;
import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.ClimberOff;
import org.usfirst.frc.team2811.robot.commands.ElevatorOff;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.IntakeIn;
import org.usfirst.frc.team2811.robot.commands.IntakeBallOff;
import org.usfirst.frc.team2811.robot.commands.IntakeBallIn;
import org.usfirst.frc.team2811.robot.commands.IntakeOut;
import org.usfirst.frc.team2811.robot.commands.ShooterOff;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;
import org.usfirst.frc.team2811.robot.commands.TurretManualTurn;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;

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
	private Button shootButton2;
	private Button climbButton;

    private Button clock;
    private Button counterClock;
    private Button turn;
    
    private Button intakeInButton;
    private Button elevatorOnButton;
    private Button intakeOutButton;
    private Button blenderOnButton;
    private Button intakeOnButton;
    private Button manualTurretControl;
   
    public OI(){
    	stick = new Joystick(1);

    	shootButton = new JoystickButton(stick,1);
    	shootButton.whileHeld(new ShooterSequence());
    	//shootButton.whileHeld(new ShooterTuning());
    	
    	shootButton2 = new JoystickButton(stick, 11);
    	shootButton2.whileHeld(new ShooterRateUpdate());
//		intakeInButton = new JoystickButton(stick,2);
//        intakeInButton.whileHeld(new IntakeIn());
        
        blenderOnButton = new JoystickButton(stick,4);
        blenderOnButton.whileHeld(new BlenderOn());
        
//        intakeOutButton = new JoystickButton(stick,3);
//        intakeOutButton.whileHeld(new IntakeOut());
        
        intakeOnButton = new JoystickButton(stick, 9);
        intakeOnButton.whileHeld(new IntakeBallIn());
        
        elevatorOnButton = new JoystickButton(stick,12);
        elevatorOnButton.whileHeld(new ElevatorOn());
        
        climbButton = new JoystickButton(stick,7);
        climbButton.whenPressed(new Climb());
        
        clock = new JoystickButton(stick, 2);
        counterClock = new JoystickButton(stick, 3);
        turn = new JoystickButton(stick,5);
        turn.whileHeld(new TurretManualTurn(0.1));
        
        manualTurretControl = new JoystickButton(stick,6);
        manualTurretControl.whileHeld(new TurretSetTargetAngle());
        
        
        
        
        
        
    }
    
    public double getShootTargetRate(){
    	return (stick.getRawAxis(3)+1)/2;
    }
    
    public double getJoystickAngle(){
    	return stick.getRawAxis(3);
    }

    public boolean isTurningClock(){
        return clock.get();
    }
    
    public boolean isTurningCounterClock(){
        return counterClock.get();
    } 
}
