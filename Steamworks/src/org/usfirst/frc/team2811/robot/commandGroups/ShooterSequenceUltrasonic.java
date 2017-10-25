package org.usfirst.frc.team2811.robot.commandGroups;

import org.usfirst.frc.team2811.robot.commands.BlenderOn;
import org.usfirst.frc.team2811.robot.commands.ElevatorOn;
import org.usfirst.frc.team2811.robot.commands.ElevatorPowerOn;
import org.usfirst.frc.team2811.robot.commands.IntakeBallIn;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;
import org.usfirst.frc.team2811.robot.commands.ShooterSetPrefsRPM;
import org.usfirst.frc.team2811.robot.commands.ShooterTuning;
import org.usfirst.frc.team2811.robot.commands.ShooterUltrasonicSetRPM;
import org.usfirst.frc.team2811.robot.commands.ShooterVisionSetRPM;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngleFromVision;
import org.usfirst.frc.team2811.robot.commands.VisionBoilerEnable;
import org.usfirst.frc.team2811.robot.commands.VisionBoilerWaitForValidTarget;
import org.usfirst.frc.team2811.robot.commands.Wait;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterSequenceUltrasonic extends CommandGroup {

    public ShooterSequenceUltrasonic() {
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
    	    	
    	//Shooter Sequence
    	addParallel(new ShooterUltrasonicSetRPM());
    	addParallel(new IntakeBallIn());
    	addParallel(new ElevatorOn());
    	addSequential(new BlenderOn());
    }
}
