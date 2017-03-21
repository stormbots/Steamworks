package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurnVision;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Wait;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Stub class! 
 * 
 * This is useful for dropping the peg onto the gear assuming the robot is 
 * roughly located where the peg is.
 * \
 *
 */
public class GearDropOnPeg extends CommandGroup {

	
    public GearDropOnPeg() {
    	
    	
    	addSequential(new GearVisionAlignment(),1.5);
    	addSequential(new ChassisDriveUltrasonic(),2.5);
    }
}
