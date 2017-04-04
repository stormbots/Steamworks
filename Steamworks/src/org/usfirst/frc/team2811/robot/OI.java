package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.AutoShooterPrefSequence;
import org.usfirst.frc.team2811.robot.commandGroups.GearDropOnPeg;
import org.usfirst.frc.team2811.robot.commandGroups.GearDropOnPegNoVision;
import org.usfirst.frc.team2811.robot.commandGroups.GearVisionAlignment;
import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequence;
import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequenceUltrasonic;
import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequenceVision;
import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoDrive;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurn;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.ClimbDown;
import org.usfirst.frc.team2811.robot.commands.ClimbSlow;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.IntakeBallIn;
import org.usfirst.frc.team2811.robot.commands.IntakeToggle;
import org.usfirst.frc.team2811.robot.commands.ShiftGears;
import org.usfirst.frc.team2811.robot.commands.ShooterOff;
import org.usfirst.frc.team2811.robot.commands.ShooterSetPrefsRPM;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;
import org.usfirst.frc.team2811.robot.commands.SketchyDriveAutoOnly;
import org.usfirst.frc.team2811.robot.commands.ToggleAutoShift;
import org.usfirst.frc.team2811.robot.commands.TurretManualTurn;
import org.usfirst.frc.team2811.robot.commands.TurretOneWayHoming;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;
import org.usfirst.frc.team2811.robot.commands.ShooterSetPrefsRPM;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

////////TWO STICK
	private Joystick leftStick;
    private JoystickButton leftTrigger;
    private JoystickButton left2;
    
    private Joystick rightStick;
    private JoystickButton rightTrigger;
    
    private boolean austinDefaultAutoShift = false;

////////XBOX    
    private XboxController xBox;
    private JoystickButton x1;
    private JoystickButton x2;
    private JoystickButton x3;
    
    private boolean connorDefaultAutoShift = false;
	
////////THREE AXIS    
    private Joystick threeAxis;
    private JoystickButton threeAxisButton1;
    private JoystickButton threeAxisButton2;
    private JoystickButton threeAxisButton3;
    private JoystickButton threeAxisButton4;
    private JoystickButton threeAxisButton5;
    private JoystickButton threeAxisButton6;
    private JoystickButton threeAxisButton7;
    private JoystickButton threeAxisButton8;
    private JoystickButton threeAxisButton9;
    private JoystickButton threeAxisButton10;
	private JoystickButton threeAxisButton11;
	private JoystickButton threeAxisButton12;

	public OI(){
////////TWO STICK    	
    	leftStick = new Joystick(0);
    	
    	leftTrigger = new JoystickButton(leftStick,1);
    	leftTrigger.whenPressed(new ShiftGears());
    	
    	left2 = new JoystickButton(leftStick,2);
    	left2.whenPressed(new ToggleAutoShift());
    	

    	rightStick = new Joystick(1);
    	
    	rightTrigger = new JoystickButton(rightStick,1);
    	rightTrigger.whileHeld(new GearDropOnPegNoVision());
////////XBOX    	
    	xBox = new XboxController(2);
    	
    	x1 = new JoystickButton(xBox,1);
    	x1.whileHeld(new GearDropOnPegNoVision());

    	x2 = new JoystickButton(xBox,2);
    	x2.whenPressed(new ShiftGears());
    	
    	x3 = new JoystickButton(xBox,3);
    	x3.whenPressed(new ToggleAutoShift());
    	
    	
////////THREE AXIS    	
		threeAxis = new Joystick(3);
		
		threeAxisButton1 = new JoystickButton(threeAxis,1);
//    	threeAxisButton1.whileHeld(new ShooterSequence(0));
    	threeAxisButton1.whileHeld(new AutoShooterPrefSequence(0));
		threeAxisButton1.whenReleased(new ShooterOff());
    	
    	threeAxisButton2 = new JoystickButton(threeAxis,2);
        threeAxisButton2.whileHeld(new TurretManualTurn());

        threeAxisButton3 = new JoystickButton(threeAxis, 3);
        threeAxisButton3.whileHeld(new IntakeBallIn());
        
        threeAxisButton4 = new JoystickButton(threeAxis,4);
        threeAxisButton4.whenPressed(new IntakeToggle());

        threeAxisButton5 = new JoystickButton(threeAxis,5);
//        threeAxisButton5.whileHeld(new GearVisionAlignment());
        threeAxisButton5.whileHeld(new ShooterSequenceVision());

        threeAxisButton6 = new JoystickButton(threeAxis,6);
        threeAxisButton6.whileHeld(new ShooterSequenceUltrasonic()); 
        
        threeAxisButton7 = new JoystickButton(threeAxis, 7);
        threeAxisButton7.whileHeld(new Climb());
        
    	threeAxisButton8 = new JoystickButton(threeAxis, 8);
    	//threeAxisButton8.whileHeld(new ElevatorOn());
    	
    	threeAxisButton8.whileHeld(new ClimbSlow());        
//      TODO Put back turretCalButton if not manual turn!
    
        
		threeAxisButton9 = new JoystickButton(threeAxis,9);
//		threeAxisButton9.whileHeld(new BlenderOn());
		threeAxisButton9.whileHeld(new ShooterSetPrefsRPM(0));

//
		threeAxisButton10 = new JoystickButton(threeAxis,10);
//		threeAxisButton10.whenPressed(new ChassisAutoDrive(4.0));
		
		threeAxisButton11 = new JoystickButton(threeAxis,11);
        threeAxisButton11.whenPressed(new TurretOneWayHoming());
        
//        threeAxisButton8 = new JoystickButton(threeAxis,8);
//        threeAxisButton8.whileHeld(new TurretManualTurn());    
    	
        threeAxisButton12 = new JoystickButton(threeAxis,12);
        threeAxisButton12.whileHeld(new GearDropOnPegNoVision());
//        threeAxisButton12.whileHeld(new ChassisDriveUltrasonic(0,24,0.2));	
        
        
        
        
	}
	
	public double getMoveValue(){
    	if(!DriverStation.getInstance().getJoystickName(2).equals("")){
    		return Math.signum(triggerMath()) * (Math.pow(triggerMath(), 2.0));
    	} else {
    		SmartDashboard.putNumber("Left Stick Move value", leftStick.getRawAxis(1));
    		return leftStick.getRawAxis(1);
    	}
    }
    
    public double getRotateValue(){
    	if(!DriverStation.getInstance().getJoystickName(2).equals("")){
    		return Robot.chassis.gearState() ? (Math.signum(xBox.getRawAxis(0)) * Math.pow(xBox.getRawAxis(0), 2)) : xBox.getRawAxis(0);	
    	} else {
    		return rightStick.getRawAxis(0) * (Robot.chassis.gearState()?.75:1.0);  		
    	}
    }
    
    /**
     * Allows Connor to drive with triggers. Makes 2 forwards and 3 backwards. 
     */
    private double triggerMath(){
    	return Robot.oi.xBox.getRawAxis(2)-Robot.oi.xBox.getRawAxis(3); 
    }
    
    public void setAutoShiftDefault(){
    	if(!DriverStation.getInstance().getJoystickName(2).equals("")){
    		Robot.chassis.setAutoShiftDefault(connorDefaultAutoShift);	
    	} else {
    		Robot.chassis.setAutoShiftDefault(austinDefaultAutoShift);	
    	}
    }
    
    public boolean isOperatorControl(){
    	return getMoveValue()>.25||getRotateValue()>.25;
    }
    
    public double getShootTargetRate(){
    	return (threeAxis.getRawAxis(3)+1)/2;
    }
    
    public double getJoystickAngle(){
    	//TODO change this back so that it works with the turret control
    	//return threeAxis.getRawAxis(3);
    	return ((5+threeAxis.getRawAxis(3))*800);
    }
    
    /**
     * Read threeAxis joystick y axis value, modify the shooter rpm to rpm+-100
     * @return a decimal between -100 and 100
     */
    public double getShooterRPMBias(){
    	return threeAxis.getRawAxis(1)*100;
    }
    public double getShooterDistanceBias(){
    	return threeAxis.getRawAxis(1)*12;
    }
    public boolean isTurningClock(){
        return threeAxisButton9.get();
    }
    
    public boolean isTurningCounterClock(){
        return threeAxisButton10.get();
    } 
}
