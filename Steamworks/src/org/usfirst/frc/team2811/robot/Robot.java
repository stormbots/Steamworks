package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commands.BlenderOff;
import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
//import org.usfirst.frc.team2811.robot.commands.ChassisDriveUltrasonic;
import org.usfirst.frc.team2811.robot.commands.Climb;
import org.usfirst.frc.team2811.robot.commands.JoystickDrive;
import org.usfirst.frc.team2811.robot.commands.ShooterRateUpdate;
import org.usfirst.frc.team2811.robot.commands.TurnToHeading;
import org.usfirst.frc.team2811.robot.commands.TurretOneWayHoming;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngle;
import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngleFromVision;
//import org.usfirst.frc.team2811.robot.commands.TurretSetTargetAngleFromVision;
import org.usfirst.frc.team2811.robot.commands.TurretTwoWayHoming;
import org.usfirst.frc.team2811.robot.subsystems.Blender;
import org.usfirst.frc.team2811.robot.subsystems.Chassis;
import org.usfirst.frc.team2811.robot.subsystems.Climber;
import org.usfirst.frc.team2811.robot.subsystems.Elevator;
import org.usfirst.frc.team2811.robot.subsystems.Gear;
import org.usfirst.frc.team2811.robot.subsystems.Intake;
import org.usfirst.frc.team2811.robot.subsystems.Shooter;
import org.usfirst.frc.team2811.robot.subsystems.Turret;
import org.usfirst.frc.team2811.robot.subsystems.Vision;

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
	public static Vision vision;
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
		vision = new Vision ();
		gear = new Gear ();
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

		chooser = new SendableChooser<Command>();
		
		joystickDrive = new JoystickDrive();

		//chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		chooser.addObject("Turret Calibration", new TurretTwoWayHoming());
		chooser.addObject("climb", new Climb());
		chooser.addObject("Shoot", new ShooterRateUpdate());
		chooser.addObject("Turret Home One way", new TurretOneWayHoming());
		chooser.addObject("Turret Set Angle", new TurretSetTargetAngle());
		chooser.addObject("Blender off", new BlenderOff() );
		chooser.addObject("Track object with turret", new TurretSetTargetAngleFromVision() );
		chooser.addObject("Drive to 3ft6in from wall", new ChassisDriveUltrasonic(0,11.3,0.5));
		//chooser.addObject("Track object with turret", new TurretSetTargetAngleFromVision() );
		//chooser.addObject("Drive to 3ft6in from wall", new ChassisDriveUltrasonic(3,6) );
		//chooser.addObject("Manual Turn", new TurretManualTurn());
		SmartDashboard.putData("Auto mode", chooser);
		}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
//		turret.updateValFromFlash();
//		shooter.updateValFromFlash();
		Robot.turret.updateValFromFlash();
		Robot.shooter.updateValFromFlash();
		Robot.blender.updateValFromFlash();
		Robot.elevator.updateValFromFlash();
		Robot.vision.updateValFromFlash();
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

		if (autonomousCommand != null)
			autonomousCommand.start();
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();		
		
		chassis.encoderReset();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		// Update the line graph on SmartDashboard *Still don't know how it updates
		SmartDashboard.putNumber("Shooter Error", Robot.shooter.getPIDError());
		SmartDashboard.putData("Compressor", compressor);
		
		chassis.getRightPosition();
		chassis.getLeftPosition();
		
		chassis.updateDashboard();
	    SmartDashboard.putNumber("Distance from wall (right,feet): ", Robot.gear.distanceRightSideInches()/12.0);
	    SmartDashboard.putNumber("Distance from wall (right,inches): ", Robot.gear.distanceRightSideInches());
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

		//System.out.println("left : "+gear.distanceLeftSide());
		//System.out.println("Right: "+gear.distanceRightSide());
	}
	
}

