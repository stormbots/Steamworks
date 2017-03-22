package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.AutoShooterSequenceWithKnownRPM;
import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequence;
import org.usfirst.frc.team2811.robot.commandGroups.TestShooterSequenceWithoutShooter;
import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoDrive;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.ClimbDown;
import org.usfirst.frc.team2811.robot.commands.ClimbSlow;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.IntakeBallIn;
import org.usfirst.frc.team2811.robot.commands.IntakeIn;
import org.usfirst.frc.team2811.robot.commands.IntakeToggle;
import org.usfirst.frc.team2811.robot.commands.ShiftGears;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;
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
public class DebugOI extends OI{
	
////////THREE AXIS    
    private Joystick logitechJoystick;
    private JoystickButton logitechJoystickButtonX;//button 1
    private JoystickButton logitechJoystickButtonA;//button 2
    private JoystickButton logitechJoystickButtonB;//button 3
    private JoystickButton logitechJoystickButtonY;//button4
    private JoystickButton logitechJoystickButtonLB;//button5
    private JoystickButton logitechJoystickButtonRB;//button6
    private JoystickButton logitechJoystickButtonLT;//button7
    private JoystickButton logitechJoystickButtonRT;//button8
    private JoystickButton logitechJoystickButtonBack;//button9
    private JoystickButton logitechJoystickButtonStart;//button10
    private JoystickButton logitechJoystickButtonLeftAxis;//button11
    private JoystickButton logitechJoystickButtonRightAxis;//button12

	public DebugOI(){    	
////////THREE AXIS    	
		logitechJoystick = new Joystick(5);
		
		logitechJoystickButtonX = new JoystickButton(logitechJoystick,1);
    	logitechJoystickButtonX.whileHeld(new AutoShooterSequenceWithKnownRPM(3000));

        logitechJoystickButtonA = new JoystickButton(logitechJoystick,2);
        logitechJoystickButtonA.whileHeld(new TurretManualTurn());
        
        logitechJoystickButtonB = new JoystickButton(logitechJoystick, 3);
        logitechJoystickButtonB.whenPressed(new TestShooterSequenceWithoutShooter(0));
        
        logitechJoystickButtonY = new JoystickButton(logitechJoystick, 4);
        logitechJoystickButtonY.whileHeld(new IntakeIn());
        
        logitechJoystickButtonLB = new JoystickButton(logitechJoystick, 5);//Turn counterclockwise with the turret

        logitechJoystickButtonRB = new JoystickButton(logitechJoystick, 6);//Turn Clockwise with the turret

        logitechJoystickButtonBack = new JoystickButton(logitechJoystick,9);
        logitechJoystickButtonBack.whileHeld(new IntakeToggle());
        
        logitechJoystickButtonStart = new JoystickButton(logitechJoystick,10);
        logitechJoystickButtonStart.whileHeld(new ClimbSlow());
     
        

//        logitechJoystickButton3 = new JoystickButton(logitechJoystick, 3);
//        logitechJoystickButton3.whileHeld(new IntakeBallIn());
//        
//        logitechJoystickButton4 = new JoystickButton(logitechJoystick,4);
//        logitechJoystickButton4.whenPressed(new IntakeToggle());
//
//        logitechJoystickButton5 = new JoystickButton(logitechJoystick,5);
//        logitechJoystickButton5.whileHeld(new BlenderOn());
//
//        logitechJoystickButton6 = new JoystickButton(logitechJoystick,6);
//        logitechJoystickButton6.whileHeld(new Climb()); 
//        
//        logitechJoystickButton7 = new JoystickButton(logitechJoystick, 7);
//        logitechJoystickButton7.whileHeld(new ClimbDown());
//        
//        logitechJoystickButton8 = new JoystickButton(logitechJoystick,8);
//        logitechJoystickButton8.whenPressed(new TurretOneWayHoming());
//                
//        
//    	logitechJoystickButton11 = new JoystickButton(logitechJoystick, 11);
//    	logitechJoystickButton11.whileHeld(new ShooterTuning());
//    	
//        logitechJoystickButton12 = new JoystickButton(logitechJoystick,12);
//        logitechJoystickButton12.whileHeld(new ElevatorOn());
//        
        logitechJoystickButtonRT = new JoystickButton(logitechJoystick, 8);
        logitechJoystickButtonRT.whenPressed(new ShiftGears());
        
        logitechJoystickButtonLT = new JoystickButton(logitechJoystick, 7);
//        logitechJoystickButtonLT.whileHeld(new ChassisDriveUltrasonic())
        
        
        
        
	}
	
	public double getMoveValue(){
		return logitechJoystick.getRawAxis(1);
    }
    
    public double getRotateValue(){
    	return logitechJoystick.getRawAxis(0);
    }

    private double triggerMath(){
    	return 0;
    }
    
    public boolean isOperatorControl(){
    	return getMoveValue()>.25||getRotateValue()>.25;
    }
    
    public double getShootTargetRate(){
    	return (logitechJoystick.getRawAxis(3)+1)/2;
    }
    
    public double getJoystickAngle(){
    	return 0;
    }

    public boolean isTurningClock(){
        return logitechJoystickButtonRB.get();
    }
    
    public boolean isTurningCounterClock(){
        return logitechJoystickButtonLB.get();
    } 

    public double getShooterValue(){
    	if(logitechJoystick.getPOV(0) == 0){
    		return 3000;
    	}
    	else if(logitechJoystick.getPOV(0) == 45){
    		return 3300;
    	}
    	else if(logitechJoystick.getPOV(0) == 90){
    		return 3500;
    	}
    	else if(logitechJoystick.getPOV(0) == 135){
    		return 3750;
    	}
    	else if(logitechJoystick.getPOV(0) == 180){
    		return 4000;
    	}
    	else if(logitechJoystick.getPOV(0) == 225){
    		return 4150;
    	}
    	else if(logitechJoystick.getPOV(0) == 270){
    		return 4250;
    	}
    	else if(logitechJoystick.getPOV(0) == 315){
    		return 4500;
    	}
    	else if(logitechJoystick.getPOV() == -1){
    		return 0;
    	}
    	else{
    		return 0;
    	}
    }
 
    
}
//    
//    public double getClimbValue(){
//    	if(logitechJoystick.getPOV(0) == 0){
//    		 return 0.8;//GET FROM PREF
//		 }
//    	else if(logitechJoystick.getPOV(0) == 180){
//    		return -0.8;
//    	}
//    	else{
//    		return 0;
//		}
//    }