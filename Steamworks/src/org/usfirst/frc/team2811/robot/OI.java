package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequence;
import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoDrive;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.ClimbDown;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.IntakeBallIn;
import org.usfirst.frc.team2811.robot.commands.IntakeToggle;
import org.usfirst.frc.team2811.robot.commands.ShiftGears;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;
import org.usfirst.frc.team2811.robot.commands.TurretManualTurn;
import org.usfirst.frc.team2811.robot.commands.TurretOneWayHoming;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
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
    public XboxController xBox;
    public JoystickButton x2;
	
////////THREE AXIS    
    public Joystick threeAxis;
    public JoystickButton threeAxisButton1;
    public JoystickButton threeAxisButton2;
    public JoystickButton threeAxisButton3;
    public JoystickButton threeAxisButton4;
    public JoystickButton threeAxisButton5;
    public JoystickButton threeAxisButton6;
    public JoystickButton threeAxisButton7;
    public JoystickButton threeAxisButton8;
    public JoystickButton threeAxisButton9;
    public JoystickButton threeAxisButton10;
	public JoystickButton threeAxisButton11;
	public JoystickButton threeAxisButton12;

	public OI(){
////////TWO STICK    	
    	leftStick = new Joystick(0);
    	
    	leftTrigger = new JoystickButton(leftStick,1);
    	leftTrigger.whenPressed(new ShiftGears());

    	rightStick = new Joystick(1);
    	
    	rightTrigger = new JoystickButton(rightStick,1);
        rightTrigger.whenPressed(new ChassisDriveUltrasonic(0,11.25,0.05001));	//TODO tune with compbot
    	
////////XBOX    	
    	xBox = new XboxController(2);
    	
    	x2 = new JoystickButton(xBox,2);
    	x2.whenPressed(new ShiftGears());
    	
////////THREE AXIS    	
		threeAxis = new Joystick(3);
		
		threeAxisButton1 = new JoystickButton(threeAxis,1);
    	threeAxisButton1.whileHeld(new ShooterSequence());
    	
        threeAxisButton2 = new JoystickButton(threeAxis,2);
        threeAxisButton2.whileHeld(new TurretSetTargetAngle());

        threeAxisButton3 = new JoystickButton(threeAxis, 3);
        threeAxisButton3.whileHeld(new IntakeBallIn());
        
        threeAxisButton4 = new JoystickButton(threeAxis,4);
        threeAxisButton4.whenPressed(new IntakeToggle());

        threeAxisButton5 = new JoystickButton(threeAxis,5);
        threeAxisButton5.whileHeld(new BlenderOn());

        threeAxisButton6 = new JoystickButton(threeAxis,6);
        threeAxisButton6.whileHeld(new Climb()); 
        
        threeAxisButton7 = new JoystickButton(threeAxis, 7);
        threeAxisButton7.whileHeld(new ClimbDown());
        
        threeAxisButton8 = new JoystickButton(threeAxis,8);
        threeAxisButton8.whenPressed(new TurretOneWayHoming());
        
//        //Put back turretCalButton if not manual turn!
//        threeAxisButton8 = new JoystickButton(threeAxis,8);
//        threeAxisButton8.whileHeld(new TurretManualTurn());
        
        threeAxisButton9 = new JoystickButton(threeAxis, 9);

        threeAxisButton10 = new JoystickButton(threeAxis, 10);
        
    	threeAxisButton11 = new JoystickButton(threeAxis, 11);
    	threeAxisButton11.whileHeld(new ShooterTuning());
    	
        threeAxisButton12 = new JoystickButton(threeAxis,12);
        threeAxisButton12.whileHeld(new ElevatorOn());
        
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

    private double triggerMath(){
    	double output = Robot.oi.xBox.getRawAxis(2)-Robot.oi.xBox.getRawAxis(3); 
    	xBox.setRumble(output>0?RumbleType.kLeftRumble:RumbleType.kRightRumble, Math.abs(output));
    	return output;
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
        return threeAxisButton9.get();
    }
    
    public boolean isTurningCounterClock(){
        return threeAxisButton10.get();
    } 
}
