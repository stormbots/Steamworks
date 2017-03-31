package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.ChassisAutoDrive;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurn;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurnVision;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBlueRightGear extends CommandGroup {

    public AutoBlueRightGear() {
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
    	addSequential(new ChassisAutoDrive(0,78),5);
    	addSequential(new ChassisAutoTurn(-60.0),2);
    	addSequential(new ChassisDriveUltrasonic(0,24,0.3),2);
    	addSequential(new GearDropOnPeg(),3.5);
    }
}
