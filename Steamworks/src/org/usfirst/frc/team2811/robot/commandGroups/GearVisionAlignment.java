package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurnVision;
import org.usfirst.frc.team2811.robot.commands.Wait;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearVisionAlignment extends CommandGroup {

    public GearVisionAlignment() {
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
    	addSequential(new Wait(0.1));
    	addSequential(new ChassisAutoTurnVision(0.5),0.75);
    	addSequential(new Wait(0.1));
    	addSequential(new ChassisAutoTurnVision(0.5),0.5);
    	addSequential(new Wait(0.1));
    	addSequential(new ChassisAutoTurnVision(0.5),0.25);
    	

    }
}
