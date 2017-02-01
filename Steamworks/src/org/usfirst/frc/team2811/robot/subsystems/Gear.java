package org.usfirst.frc.team2811.robot.subsystems;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/** #### Sensors/Actuators
- UltraSonicLeft
- UltraSonicRight
- Servo 
- GearSwitch 

#### Functions 
- isGearPresent()
- DistanceLeft()
- DistanceRight()
- open()
- close()
 *
 */
public class Gear extends Subsystem {
	private Ultrasonic sonarLeft;
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public Gear() {
		sonarLeft = new Ultrasonic(0,1);
		sonarLeft.setEnabled(true);
		sonarLeft.setAutomaticMode(true);
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public double distanceLeftSide(){
    	return sonarLeft.getRangeInches();
    }
}

