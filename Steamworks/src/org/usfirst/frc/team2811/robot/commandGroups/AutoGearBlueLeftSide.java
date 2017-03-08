package org.usfirst.frc.team2811.robot.commandGroups;


import org.usfirst.frc.team2811.robot.commands.ChassisAutoDrive;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurn;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurnVision;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Wait;
import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 *  Drive 7 ft, Wait, if valid Target turn with Vision until centered, GearDropOnPeg
 */
public class AutoGearBlueLeftSide extends CommandGroup {

    public AutoGearBlueLeftSide() {
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
    	
    	addSequential(new ChassisAutoDrive(0,109.5),5);
    	addSequential(new ChassisAutoTurn(-60.0),2);
    	addSequential(new ChassisAutoDrive(0,12),4);
//    	addSequential(new ChassisAutoTurnVision(0.5),2);
    	addSequential(new GearDropOnPegWithoutVision());
    	
    }
}
