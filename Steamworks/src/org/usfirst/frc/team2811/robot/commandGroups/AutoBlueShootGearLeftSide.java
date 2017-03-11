package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.ShooterSetPrefsRPM;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBlueShootGearLeftSide extends CommandGroup {

    public AutoBlueShootGearLeftSide() {
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
    	addSequential(new ShooterSetPrefsRPM(3750), 1.5);
    	addSequential(new AutoShooterPrefSequence(3750), 4.0);
    	addSequential(new AutoGearBlueLeftSide(), 15.0);
    }
}
