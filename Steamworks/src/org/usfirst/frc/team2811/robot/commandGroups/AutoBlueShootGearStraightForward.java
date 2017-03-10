package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.BlenderOff;
import org.usfirst.frc.team2811.robot.commands.ElevatorOff;
import org.usfirst.frc.team2811.robot.commands.ShooterSetRPM;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBlueShootGearStraightForward extends CommandGroup {

    public AutoBlueShootGearStraightForward() {
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
    	addSequential(new ShooterSetRPM(4250), 0.1);
    	addSequential(new AutoShooterSequence(4250), 2.75);
    	addSequential(new ShooterSetRPM(0),0);
    	addSequential(new BlenderOff(),0);
    	addSequential(new ElevatorOff(),0);
    	addSequential(new AutoGearStraightForward(), 10);
    }
}
