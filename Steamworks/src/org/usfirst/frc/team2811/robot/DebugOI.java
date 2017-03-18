package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.AutoShooterSequenceWithKnownRPM;
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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class DebugOI extends OI{
	
////////THREE AXIS    
    public Joystick logitechJoystick;
    public JoystickButton logitechJoystickButtonX;//button 1
    public JoystickButton logitechJoystickButtonA;//button 2
    public JoystickButton logitechJoystickButtonB;//button 3
    public JoystickButton logitechJoystickButtonY;//button4
    public JoystickButton logitechJoystickButtonLB;//button5
    public JoystickButton logitechJoystickButtonRB;//button6
    public JoystickButton logitechJoystickButtonLT;//button7
    public JoystickButton logitechJoystickButtonRT;//button8
    public JoystickButton logitechJoystickButtonBack;//button9
    public JoystickButton logitechJoystickButtonStart;//button10
	public JoystickButton logitechJoystickButtonRightAxis;//button11
	public JoystickButton logitechJoystickButtonLeftAxis;//button12

	public DebugOI(){    	
////////THREE AXIS    	
		logitechJoystick = new Joystick(5);
		
		logitechJoystickButtonX = new JoystickButton(logitechJoystick,1);
    	logitechJoystickButtonX.whileHeld(new AutoShooterSequenceWithKnownRPM(3000));

        logitechJoystickButtonA = new JoystickButton(logitechJoystick,2);
        logitechJoystickButtonA.whileHeld(new TurretSetTargetAngle());

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
        logitechJoystickButtonRB = new JoystickButton(logitechJoystick, 6);

        logitechJoystickButtonLB = new JoystickButton(logitechJoystick, 5);
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
		return logitechJoystick.getRawAxis(0);
    }
    
    public double getRotateValue(){
    	return logitechJoystick.getRawAxis(1);
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
    
    public void climbUp(){
    	if(logitechJoystick.getPOV() == 0){
    		 Robot.climber.climbUp();	
    	}
    	
    	else if(!(logitechJoystick.getPOV() == 0)){
    		Robot.climber.climberOff();
  	
    	}
    }
    
    public void intakeOnOff(){
    	if(logitechJoystick.getPOV() == 45 ){
    		Robot.intake.setIntakeOff();
  
    	}
    	else if(!(logitechJoystick.getPOV() == 45)){  
    		Robot.intake.setIntakeOff();
    	}
    }
    
    
    
    public void povButtonAssignment(double POV ){
    	if(logitechJoystick.getPOV() == POV){
    		   		
    	}
    }
}
