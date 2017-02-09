package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.commands.ManualDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * Expands RobotDrive to allow for MiniPID control of each output
 */
public class Turret extends Subsystem {

	//Access preference on the SmartDashboard
	Preferences prefs = Preferences.getInstance();
	
	//Save data into preference
    private int upTicks = prefs.getInt("upTicks", 12421);
	private int downTicks = prefs.getInt("downTicks", -2850);
    private int upAngle = 180;
    private int downAngle = 0;
    private int downJoystick = -1;
    private int upJoystick = 1;

    private double currentOutput;
    private double stallCurrentOffset = 0.2;
    private double homingSpeed = 0.2;
    private CANTalon turretMotor;

    private boolean encPosSet = false;
    private boolean upTicksSet = false;
    private boolean downTicksSet = false;
       
	public Turret(){
		turretMotor = new CANTalon(4);
        turretMotor.reset();
    	turretMotor.clearStickyFaults();
    	turretMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	turretMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	turretMotor.enable();
    	turretMotor.set(0);
	}
	
	protected void initDefaultCommand() {
		setDefaultCommand(new ManualDrive(1));
	}

	//Homing using only one limit switch
    public void oneWayHoming(){
    	turretMotor.set(-homingSpeed); //Stop when run counter clockwise
    	currentOutput = turretMotor.getOutputCurrent();
    	if(turretMotor.getOutputCurrent() - currentOutput > stallCurrentOffset){
    		turretMotor.setEncPosition(upTicks);
    		encPosSet = true;
    	}
    }
    
  //Homing using two limit switch
    public void twoWayHoming(){
    	turretMotor.set(homingSpeed);
    	currentOutput = turretMotor.getOutputCurrent();
    	
    	if(!upTicksSet){
    		turretMotor.set(homingSpeed); //Stop when run counter clockwise
    		if(turretMotor.getOutputCurrent() - currentOutput > stallCurrentOffset){
    			turretMotor.set(-homingSpeed);
    			currentOutput = turretMotor.getOutputCurrent();
    			//turretMotor.setEncPosition(upTicks);
    			//prefs.putInt("upTicks", upTicks);
    			upTicks = turretMotor.getEncPosition();
    			upTicksSet = true;
    			System.out.println("upTicksSet " + upTicks);
    		}
    	}else if(!downTicksSet){
    		turretMotor.set(-homingSpeed);
    		if(turretMotor.getOutputCurrent() - currentOutput > stallCurrentOffset){
    			turretMotor.set(0);
    			//turretMotor.setEncPosition(downTicks);
    			//prefs.putInt("downTickSet", downTicks);
    			downTicks = turretMotor.getEncPosition();
    			downTicksSet = true;
    			System.out.println("downTicksSet " + downTicks);
    		}
    	}else if(upTicksSet&&downTicksSet){
    		encPosSet = true;
    	}
    }
/**  
  //TODO Make (Joystick buttons for these functions like one way homing
   * two way homing, turn clockwise manual, counterclockwise, auto turn(using vision)
   * manual turn)
   * 
   * 
    //Turn the turret manually with button 1 and 2
    public void manualTurn(){
    	System.out.println("encPos:" + turretMotor.getEncPosition());
        if(Robot.oi.isTurningClockwise()){        	
        	turretMotor.set(0.2);
        }else if(Robot.oi.isTurningCounterClockwise()){
        	turretMotor.set(-0.2);
        }else{
        	turretMotor.set(0);
        }
    }
    */
    public boolean isHomed(){
    	return encPosSet;
    }
    
    public double getCurrentAngle(){
    	return ticksToAngle(turretMotor.getEncPosition());
    }

    public double ticksToAngle(int ticks){
    	return map(ticks, downTicks, upTicks, downAngle, upAngle);
    }
    
    public double joystickToAngle(double input){
    	return map(input, downJoystick, upJoystick, downAngle, upAngle);
    }
    
    public int angleToTicks(double angle){
    	return (int)map(angle, downAngle, upAngle, downTicks, upTicks);
    }
    
    public double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
    
    public void setTurretMotor(double motorOutput){
    	turretMotor.set(motorOutput);
    }
}