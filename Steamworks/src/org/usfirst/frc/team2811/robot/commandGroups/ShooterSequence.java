package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.ElevatorPowerOn;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;
import org.usfirst.frc.team2811.robot.commands.Wait;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterSequence extends CommandGroup {

    public ShooterSequence() {
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
    	
    	//TODO: map a button to whileHeld()
    	//TODO: Need to add vision to this EX: Turn to target(chassis)
    	
    	//addSequential(new TurretSetTargetAngle());
    	//addSequential(new Wait(0.5));
    	addSequential(new ShooterTuning(), 0.75);
    	addParallel(new ShooterTuning());
//    	addSequential(new Wait(0.5));
    	//Both blender and elevator are set to a hard coded value
    	//addParallel(new ElevatorOn());
    	addParallel(new ElevatorPowerOn());
    	//addSequential(new BlenderOn());
    }
}
