package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.BlenderOff;
import org.usfirst.frc.team2811.robot.commands.ElevatorOff;
import org.usfirst.frc.team2811.robot.commands.ShooterAutoSetRPM;
import org.usfirst.frc.team2811.robot.commands.ShooterOff;
import org.usfirst.frc.team2811.robot.commands.ShooterSetPrefsRPM;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRedRightShootGear extends CommandGroup {

    public AutoRedRightShootGear() {
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
    	addSequential(new ShooterAutoSetRPM(3700), 0.1);
//    	addSequential(new AutoShooterSequenceWithKnownRPM(3650), 2.75);
    	addSequential(new AutoShooterSequenceWithKnownRPM(3700), 8.0);
    	addSequential(new ShooterOff(),0.1);
    	addSequential(new BlenderOff(),0.1);
    	addSequential(new ElevatorOff(),0.1);
    	addSequential(new AutoRedRightGear());
    }
}
