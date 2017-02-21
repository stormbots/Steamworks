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
    	return sonarLeft.getRangeInches();
    }
    
    public double distanceRightSideInches(){
    	return sonarRight.getRangeInches();
    }
    
    
}

