package org.usfirst.frc.team2811.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/** 
 *
 */
public class Gear extends Subsystem {
	private Ultrasonic sonarLeft;
	private Ultrasonic sonarRight;
	private Servo servo;
	private boolean doorOpen;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public Gear() {
		sonarLeft = new Ultrasonic(0,1);
		sonarLeft.setEnabled(true);
		sonarLeft.setAutomaticMode(true);
		
		sonarRight = new Ultrasonic(2,3);
		sonarRight.setEnabled(true);
		sonarRight.setAutomaticMode(true);
		
		servo = new Servo(10);
		doorOpen = false;
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public double distanceLeftSide(){
    	return sonarLeft.getRangeInches();
    }
    
    public double distanceRightSide(){
    	return sonarRight.getRangeInches();
    }
    
    public void close(){
    	servo.setPosition(0);
    	doorOpen=false;
    }
    
    public void open(){
    	servo.setPosition(1);
    	doorOpen=true;
    }

	public boolean isOpen() {
		return doorOpen;
	}
}

