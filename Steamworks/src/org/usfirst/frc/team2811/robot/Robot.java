package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.AutoGearStraightForward;
//import org.usfirst.frc.team2811.robot.commandGroups.AutoRedShootTurnDrive;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftSideShootDriveForwardPastBaseLine;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueShootGearLeftSide;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueShootGearRightSide;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueShootGearStraightForward;
import org.usfirst.frc.team2811.robot.commandGroups.AutoDriveForward10ft;
import org.usfirst.frc.team2811.robot.commandGroups.AutoDriveForward60inches;
import org.usfirst.frc.team2811.robot.commandGroups.AutoGearBlueLeftSide;
import org.usfirst.frc.team2811.robot.commandGroups.AutoGearBlueRightSide;
import org.usfirst.frc.team2811.robot.commandGroups.AutoGearRedLeftSide;
import org.usfirst.frc.team2811.robot.commandGroups.AutoGearRedRightSide;
import org.usfirst.frc.team2811.robot.commandGroups.GearDropOnPeg;
import org.usfirst.frc.team2811.robot.commands.BlenderOff;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.JoystickDrive;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;
import org.usfirst.frc.team2811.robot.commands.TurnToHeading;
import org.usfirst.frc.team2811.robot.commands.TurretOneWayHoming;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngleFromVision;
import org.usfirst.frc.team2811.robot.commands.TurretTwoWayHoming;
import org.usfirst.frc.team2811.robot.commands.Wait;
import org.usfirst.frc.team2811.robot.subsystems.Blender;
import org.usfirst.frc.team2811.robot.subsystems.Chassis;
import org.usfirst.frc.team2811.robot.subsystems.Climber;
import org.usfirst.frc.team2811.robot.subsystems.Elevator;
import org.usfirst.frc.team2811.robot.subsystems.Gear;
import org.usfirst.frc.team2811.robot.subsystems.Intake;
import org.usfirst.frc.team2811.robot.subsystems.Shooter;
import org.usfirst.frc.team2811.robot.subsystems.Turret;
import org.usfirst.frc.team2811.robot.subsystems.VisionBoiler;
import org.usfirst.frc.team2811.robot.subsystems.VisionGear;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	//Declare subsystems
	public static VisionBoiler visionBoiler;
	public static VisionGear visionGear;
	public static Gear gear;
	public static Climber climber;
	public static Shooter shooter;
	public static Turret turret;
	public static Elevator elevator;
	public static Compressor compressor;
	public static Intake intake;
	public static Chassis chassis;
	public static Blender blender;
	public static OI oi;	
	SendableChooser<OI> oiChooser;

	Command autonomousCommand;
	SendableChooser<Command> autonomousChooser;

	//Debug Commands

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {		
		//Initialize Subsystems
		visionBoiler = new VisionBoiler();
		visionGear = new VisionGear();
		gear = new Gear();
		climber = new Climber();
		shooter = new Shooter();
		turret = new Turret();
		elevator = new Elevator();
		compressor = new Compressor();
		intake = new Intake();
		blender = new Blender();
		chassis = new Chassis();

		//ALWAYS INITIALIZE ALL SUBSYSTEMS BEFORE OI, or requires() doesn't work
		oi = new OI();
		oiChooser = new SendableChooser<OI>();
		//oiChooser.addDefault("Driver OI", new OI());
		oiChooser.addObject("Driver OI", new OI());
		oiChooser.addObject("Debug OI", new DebugOI());
		SmartDashboard.putData("Operator Interface", oiChooser);

		
		//autonomousChooser.addObject("Manual Turn", new TurretManualTurn());
//		SmartDashboard.putData("Auto mode", autonomousChooser);
		
		autonomousChooser = new SendableChooser<Command>();
		//autonomousChooser.addObject("Blue Drop Gear From Left Side", new AutoGearBlueLeftSide());
		//autonomousChooser.addObject("climb", new Climb());

		//autonomousChooser.addObject("Blue Drop Gear From Right Side", new AutoGearBlueRightSide());
		autonomousChooser.addObject("Blue Drop Gear Straight Forward", new AutoGearStraightForward());
		autonomousChooser.addObject("Drive Forward 10 feet", new AutoDriveForward10ft());
		autonomousChooser.addObject("Drive Forward 60inches", new AutoDriveForward60inches());
		autonomousChooser.addObject("Blue Shoot Straight Forward", new AutoBlueLeftSideShootDriveForwardPastBaseLine());
		autonomousChooser.addObject("Blue Shoot Drop Gear Straight Forward", new AutoBlueShootGearStraightForward());
		//autonomousChooser.addDefault("Red shoot and turn drive", new AutoRedShootTurnDrive());
		//autonomousChooser.addObject("Blue Shoot Drop Gear From Left Side", new AutoBlueShootGearLeftSide());
		//autonomousChooser.addObject("Blue Shoot Drop Gear From Right Side", new AutoBlueShootGearRightSide());
		//autonomousChooser.addObject("Red Drop Gear From Right Side", new AutoGearRedRightSide());
		//autonomousChooser.addObject("Red Drop Gear From Left Side", new AutoGearRedLeftSide());
		
//		autonomousChooser.addObject("vvvv DEBUG COMMANDS vvv", new Wait(0));
//		autonomousChooser.addObject("Turret Calibration", new TurretOneWayHoming());
		autonomousChooser.addObject("climb", new Climb());
//		autonomousChooser.addObject("Shoot", new ShooterRateUpdate());
//		autonomousChooser.addObject("Turret Home One way", new TurretOneWayHoming());
//		autonomousChooser.addObject("Turret Set Angle", new TurretSetTargetAngle());
//		autonomousChooser.addObject("Turret Track object with vision", new TurretSetTargetAngleFromVision() );
//		autonomousChooser.addObject("Blender off", new BlenderOff() );
//		autonomousChooser.addObject("Drive to 3ft6in from wall", new ChassisDriveUltrasonic(0,11.3,0.5));
		//autonomousChooser.addObject("Track object with turret", new TurretSetTargetAngleFromVision() );
		//autonomousChooser.addObject("Drive to 3ft6in from wall", new ChassisDriveUltrasonic(3,6) );
		//autonomousChooser.addObject("Manual Turn", new TurretManualTurn());
		SmartDashboard.putData("Auto mode", autonomousChooser);
		Robot.intake.intakeIn();
		}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		Robot.intake.intakeIn();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();

		visionGear.heartbeat();
		
		Robot.turret.updateValFromFlash();
		Robot.shooter.updateValFromFlash();
		Robot.blender.updateValFromFlash();
		Robot.elevator.updateValFromFlash();
		Robot.chassis.updateValFromFlash();
		Robot.visionBoiler.updateValFromFlash();
		Robot.visionGear.updateValFromFlash();
	}
	
	
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		Util.updateFlash();
		chassis.setGearLow();
		autonomousCommand = autonomousChooser.getSelected();

		if (autonomousCommand != null) autonomousCommand.start();
		chassis.encoderReset();
		Robot.intake.intakeOut();

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
//		visionGear.heartbeat();
		gear.updateDashboard();
		chassis.updateDashboard();
		visionBoiler.update();
		visionGear.update();
		SmartDashboard.putNumber("Vision Gear Angle", Robot.visionGear.getAngleHorizontal());
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)autonomousCommand.cancel();

		OI newOI=oiChooser.getSelected();
		if(newOI != null) oi = newOI;

		Util.updateFlash();
		chassis.setGearLow();

		oi.setAutoShiftDefault();

		Robot.intake.intakeOut();
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		// Important! This talks to the RasPi so our vision works
		visionBoiler.update();
		visionGear.update();
//		visionGear.heartbeat();
		
		gear.updateDashboard();
		chassis.updateDashboard();
		// Update the line graph on SmartDashboard *Still don't know how it updates
		// SmartDashboard.putNumber("Shooter Error", Robot.shooter.getPIDError());
		SmartDashboard.putData("Compressor", compressor);
        SmartDashboard.putNumber("TurretPos", Robot.turret.getCurrentPos());
        SmartDashboard.putNumber("Turret Current Angle", Robot.turret.getCurrentAngle());
        //SmartDashboard.putNumber("Turret Target Angle", Robot.turret.joystickToAngle(Robot.oi.getJoystickAngle()));
		//SmartDashboard.putNumber("Turret output", Robot.turret.getOutput());
        
        SmartDashboard.putNumber("Vision distance boiler", visionBoiler.getDistanceTargetBoiler());
		SmartDashboard.putNumber("Vision angle to boiler", visionBoiler.getAngleTargetHorizontalBoiler());
        
	    SmartDashboard.putNumber("Distance from wall (right,feet): ", Robot.gear.getDistanceFeet());
	    SmartDashboard.putNumber("Distance from wall (right,inches): ", Robot.gear.getDistanceInches());
	    
	    SmartDashboard.putNumber("Shooter speed error", -Robot.shooter.getPIDError());

		}

	
	public void testInit() {
		LiveWindow.run();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();

	}
	
}

