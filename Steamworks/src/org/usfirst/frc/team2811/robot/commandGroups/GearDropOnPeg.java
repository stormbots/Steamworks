package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurnVision;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Wait;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  
 * 
 * This is useful for align the robot with vision and drop the gear
 * \
 *
 */
public class GearDropOnPeg extends CommandGroup {

	/**
	 * 1.5sec Vision
	 * 2.2sec Ultrasonic drive
	 */
    public GearDropOnPeg() {
    	
    	
    	addSequential(new GearVisionAlignment(),1.5);
    	addSequential(new ChassisDriveUltrasonic(),2.2);
    }
}
