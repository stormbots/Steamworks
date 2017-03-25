package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.AutoCenterGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedShootTurnMobility;
import org.usfirst.frc.team2811.robot.commandGroups.AutoCenterGear;
//import org.usfirst.frc.team2811.robot.commandGroups.AutoRedShootTurnDrive;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftShootMobility;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueRightShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueCenterShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoMobility10ft;
import org.usfirst.frc.team2811.robot.commandGroups.AutoMobility60inches;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueRightGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedLeftGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.GearDropOnPeg;
import org.usfirst.frc.team2811.robot.commandGroups.GearVisionAlignment;
import org.usfirst.frc.team2811.robot.commands.BlenderOff;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoTurn;
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

	Command joystickDrive;
	
	Command autonomousCommand;
	SendableChooser<Command> chooser;

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

		
		joystickDrive = new JoystickDrive();
		
		chooser = new SendableChooser<Command>();
//		chooser.addDefault("climb", new Climb());

		//BLUE
		chooser.addObject("Blue Right Gear", new AutoBlueRightGear());
		chooser.addObject("Blue Left Shoot Mobility", new AutoBlueLeftShootMobility());
		chooser.addObject("Blue Center Shoot Gear", new AutoBlueCenterShootGear());
		chooser.addObject("Blue Left Shoot Gear", new AutoBlueLeftShootGear());
		
		//RED
		chooser.addObject("Red Right Shoot Turn Mobility", new AutoRedShootTurnMobility());
		chooser.addObject("Red Right Shoot Turn Gear", new AutoRedRightShootGear());
		chooser.addObject("Red Right Gear", new AutoRedRightGear());
		chooser.addObject("Red Left Gear", new AutoRedLeftGear());
		
		//NEUTRAL
		chooser.addDefault("Blue/Red Center Gear", new AutoCenterGear());
		chooser.addObject("Mobility 10 feet", new AutoMobility10ft());
		chooser.addObject("Mobility 60 inches", new AutoMobility60inches());
		
//		//TESTING & DEBUGGING
		chooser.addObject("Vision Alignment", new GearVisionAlignment());
//		chooser.addObject("Turn 8deg", new ChassisAutoTurn(8));
//		chooser.addObject("Turn 90deg", new ChassisAutoTurn(90));
//		chooser.addObject("Turn 135deg", new ChassisAutoTurn(135));
//		
		SmartDashboard.putData("Auto mode", chooser);
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
		chassis.setAutoShiftEnabled(false);
		autonomousCommand = chooser.getSelected();
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
		Util.updateFlash();
		
		oi.setAutoShiftDefault();
		chassis.setGearLow();
		chassis.encoderReset();
	//.homeCW();
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

