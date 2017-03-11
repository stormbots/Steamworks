package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.ShooterSetPrefsRPM;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoShooterPrefSequence extends CommandGroup {

    public AutoShooterPrefSequence(double rpm) {
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
    	
    	addParallel(new ShooterSetPrefsRPM(rpm));
    	//addParallel(new IntakeBallIn());
//    	addSequential(new Wait(0.5));
    	//Both blender and elevator are set to a hard coded value
    	//addParallel(new ElevatorOn());
    	addParallel(new ElevatorOn());
    	addSequential(new BlenderOn());
    }
}
