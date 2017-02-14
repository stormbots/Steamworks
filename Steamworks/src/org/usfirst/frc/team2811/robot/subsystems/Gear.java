package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Robot;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/** 
 *
 */
public class Gear extends Subsystem {
	private Ultrasonic sonarLeft;
	private Ultrasonic sonarRight;
	private CANTalon motor;
  	
	double ticksPerRotation;
	
	enum DoorState{
		Opening,
		Open,
		Closed,
		Closing,
	}
	DoorState doorState;
	double doorTargetPosition=0;
		
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public Gear() {

		sonarLeft = new Ultrasonic(0,1);
		sonarLeft.setEnabled(true);
		
		sonarRight = new Ultrasonic(2,3);		     
		sonarRight.setEnabled(true);
		
		//Apparently setAutomaticMode must be called after all ultrasonics 
		//are initialized
		sonarLeft.setAutomaticMode(true);
		sonarRight.setAutomaticMode(true);
		
		doorState = DoorState.Closed;
		//ticksPerRotation = Robot.prefs.getDouble("GearDoorTicksPerRotation", 1);
		ticksPerRotation = 1;

		motor = new CANTalon(4);
		motor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		motor.clearStickyFaults();
		motor.enableBrakeMode(true);
		motor.reset();
		motor.enable();
		motor.set(0);
		motor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		motor.setEncPosition(0);
		
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
    
    public void toggle(){
    	rotate180();
    }
    public void close(){
    	if(doorState!=DoorState.Closed){    	
        	rotate180();
    	}    	
    }
    
    public void open(){
    	if(doorState!=DoorState.Open){    	
        	rotate180();
    	}    	
    	
    }
    
    public void rotate180(){
    	if(doorState==DoorState.Closing){
    		motor.set(.5);
    		if(motor.getPosition()>doorTargetPosition){
        		motor.set(0);
        		doorState=DoorState.Closed;    			
    		}
    		
    	}else if(doorState==DoorState.Closed){
    		doorTargetPosition += ticksPerRotation/2.0;
    		doorState=DoorState.Opening;
    		
    	}else if(doorState==DoorState.Opening){
    		motor.set(.5);
    		if(motor.getPosition()>doorTargetPosition){
        		motor.set(0);
        		doorState=DoorState.Open;    			
    		}
    		
    	}else if(doorState==DoorState.Open){
    		doorTargetPosition += ticksPerRotation/2.0;
    		doorState=DoorState.Closing;
    	}
    }    

	public boolean isOpen() {
		return doorState==DoorState.Open;
	}
	public boolean isClosed() {
		return doorState==DoorState.Closed;
	}
}

