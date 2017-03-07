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
		
		
		
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	
    }
    
    public double distanceLeftSideInches(){
    	if(sonarLeft.getRangeInches() < 2){
    		return 120;
    	}else{
    		return sonarLeft.getRangeInches();
    	}
    	
    }
    
    public double distanceRightSideInches(){
    	if(sonarRight.getRangeInches() < 2){
    		return 120;
    	}else{
    		return sonarRight.getRangeInches();
    	}
    }
    
    public double getDistanceInches(){
//    	boolean leftValid = true;
//    	boolean rightValid = true;
//    	if(distanceLeftSideInches()>=120)leftValid=false;
//    	if(distanceRightSideInches()>=120)rightValid=false;
//    	
//    	if(leftValid && rightValid){
//    		
//    		//NOTE: We would expect to divide by 2 here. 
//    		//We're not sure why we have to divide by 2 again, but it apparently fixes things
//        	return (distanceLeftSideInches()+distanceRightSideInches())/2.0/2.0;
//    	}
//    	else if(leftValid){
//    		return distanceLeftSideInches();
//    	}
//    	else if(rightValid){
//    		return distanceRightSideInches();
//    	}
//    	else return 120;
    	return distanceRightSideInches();
    }
    
    public double getDistanceFeet(){
    	return getDistanceInches()/12.0;
    }
    
    
}

