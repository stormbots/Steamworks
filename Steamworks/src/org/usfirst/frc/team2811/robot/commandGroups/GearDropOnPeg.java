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

    public GearDropOnPeg(double inches) {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.
    	
        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	
//    	addSequential(new ChassisAutoTurnVision(0.5),1.5);
    	
    	addSequential(new GearVisionAlignment(),1);
    	addSequential(new ChassisDriveUltrasonic(),3);
//    	addSequential(new ChassisDriveUltrasonic(0,9,0.3),2);
//    	addSequential(new ChassisDriveUltrasonic(0,11,0.3),2);
    }
}
