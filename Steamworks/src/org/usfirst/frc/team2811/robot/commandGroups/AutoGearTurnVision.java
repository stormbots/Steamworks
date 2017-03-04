package org.usfirst.frc.team2811.robot.commandGroups;


import org.usfirst.frc.team2811.robot.commands.ChassisAutoDrive;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurnVision;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Wait;
import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 *  Drive 7 ft, Wait, if valid Target turn with Vision until centered, GearDropOnPeg
 */
public class AutoGearTurnVision extends CommandGroup {

    public AutoGearTurnVision() {
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
    	
    	addSequential(new ChassisAutoDrive(7.5),4);
//    	addSequential(new ChassisDriveUltrasonic(7,6,0.3),4);	
//    	addSequential(new Wait(2));
//    	if(!Robot.visionGear.haveValidTargetGear()){
//    		addSequential(new ChassisAutoDrive(-1),4);
//   		addSequential(new ChassisAutoTurn(5.0));
//    		addSequential(new Wait(1));
//    	}
    	addSequential(new ChassisAutoTurnVision(0.5),2);
//    	addSequential(new Wait(2));
    	addSequential(new GearDropOnPeg(),3);
    	
    }
}
