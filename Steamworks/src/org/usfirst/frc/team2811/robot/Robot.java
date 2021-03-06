package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.commandGroups.AutoCenterGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoCenterGearWait;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedShootTurnMobility;
import org.usfirst.frc.team2811.robot.commandGroups.AutoCenterGear;
//import org.usfirst.frc.team2811.robot.commandGroups.AutoRedShootTurnDrive;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftShootMobility;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftShootLongGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueRightShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueCenterShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoMobility10ft;
import org.usfirst.frc.team2811.robot.commandGroups.AutoMobility60inches;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueLeftGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoBlueRightGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedLeftGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightGearBack;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightShootGearBack;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightShootLongGearBack;
import org.usfirst.frc.team2811.robot.commandGroups.AutoRedRightLongShootGear;
import org.usfirst.frc.team2811.robot.commandGroups.GearDropOnPeg;
import org.usfirst.frc.team2811.robot.commandGroups.GearVisionAlignment;
import org.usfirst.frc.team2811.robot.commandGroups.ShooterSequenceVision;
import org.usfirst.frc.team2811.robot.commands.BlenderOff;
import org.usfirst.frc.team2811.robot.commands.ChassisAutoDrive;
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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
	
	public static PowerDistributionPanel PDP;
	
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
		
		System.out.println("COMMENT OUT BATTERY VOLTAGE SECURE CODE");
		
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

		PDP = new PowerDistributionPanel();
		
		joystickDrive = new JoystickDrive();
		
		chooser = new SendableChooser<Command>();
//		chooser.addDefault("climb", new Climb());

		//**************************
	    // Autonomous chooser
		// Naming scheme: Color(Blue/Red Side)/Starting position from the back of the driver station/Action
		// Mobility = move past baseline
	    //*************************
		
		//BLUE
		chooser.addObject("Blue Right Gear", new AutoBlueRightGear());
		chooser.addObject("Blue Left Gear", new AutoBlueLeftGear());
		//Shoot for 7 seconds
		chooser.addObject("Blue Left Shoot Mobility", new AutoBlueLeftShootMobility());
		chooser.addObject("Blue Center Shoot Gear", new AutoBlueCenterShootGear());
		//Shoot for 2.75 seconds
		chooser.addObject("Blue Left Shoot Gear", new AutoBlueLeftShootGear());
		//Shoot for 5 seconds
		chooser.addObject("Blue Left SHOOT LONG gear", new AutoBlueLeftShootLongGear());
		
		//RED
		//Shoot for 4 seconds
		chooser.addObject("Red Right Shoot Turn Mobility", new AutoRedShootTurnMobility());
		//Shoot for 2.75 seconds
		chooser.addObject("Red Right Shoot Turn Gear", new AutoRedRightShootGear());
		//Shoot for 6 seconds
		chooser.addObject("Red Righ SHOOT LONG Turn Gear", new AutoRedRightLongShootGear());
		chooser.addObject("Red Right Gear", new AutoRedRightGear());
		chooser.addObject("Red Left Gear", new AutoRedLeftGear());
		//Shoot for 2 seconds
		chooser.addObject("Red Right Shoot Gear BACK", new AutoRedRightShootGearBack());
		//Shoot for 5 seconds
		chooser.addObject("Red Right SHOOT LONG Gear BACK", new AutoRedRightShootLongGearBack());
		chooser.addObject("Red Right Gear BACK", new AutoRedRightGearBack());
		
		//NEUTRAL
		chooser.addObject("Blue/Red Center Gear", new AutoCenterGear());
		chooser.addDefault("Blue/Red Wait Center Gear", new AutoCenterGearWait());
		//Useful for drive train calibration
		chooser.addObject("Mobility 10 feet", new AutoMobility10ft());
		chooser.addObject("Mobility 60 inches", new AutoMobility60inches());
		chooser.addObject("Debugging 3feet", new ChassisAutoDrive(3.0,0));
	//  chooser.addObject("Blue/Red Center Gear Slow Back", new AutoCenterGearWait());
		
//		//TESTING & DEBUGGING
		chooser.addObject("Vision Alignment", new GearVisionAlignment());
		chooser.addObject("Shoot W/Vision", new ShooterSequenceVision());
		chooser.addObject("Turn 8deg", new ChassisAutoTurn(8));
		chooser.addObject("Turn 90deg", new ChassisAutoTurn(90));
		chooser.addObject("Turn 135deg", new ChassisAutoTurn(135));
//		
		SmartDashboard.putData("Auto mode", chooser);
		
		Robot.intake.intakeIn();
		NetworkTable.setUpdateRate(0.02); // 20ms
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		Robot.intake.intakeIn();
		visionBoiler.disable();
		chassis.setGearLow();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();

		//visionGear.heartbeat();
		
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
		Util.checkBatteryVoltage();
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
		shooter.updateDashboard();
		visionGear.updateDashboard();

		SmartDashboard.putNumber("Vision Gear Angle", Robot.visionGear.getAngleHorizontal());
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)autonomousCommand.cancel();
		Util.checkBatteryVoltage();
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

		//visionGear.heartbeat();
		
		gear.updateDashboard();
		chassis.updateDashboard();
		visionGear.updateDashboard();
		shooter.updateDashboard();
		chassis.setLow5sec();
				
		SmartDashboard.putNumber("Time Left", DriverStation.getInstance().getMatchTime());
		
		// Update the line graph on SmartDashboard *Still don't know how it updates
		// SmartDashboard.putNumber("Shooter Error", Robot.shooter.getPIDError());
		SmartDashboard.putData("Compressor", compressor);
		SmartDashboard.putData("PDP", PDP);
		
		//TODO put them in a function in the right subsystem
        SmartDashboard.putNumber("TurretPos", Robot.turret.getCurrentPos());
        SmartDashboard.putNumber("Turret Current Angle", Robot.turret.getCurrentAngle());
        //SmartDashboard.putNumber("Turret Target Angle", Robot.turret.joystickToAngle(Robot.oi.getJoystickAngle()));
		//SmartDashboard.putNumber("Turret output", Robot.turret.getOutput());
        
        SmartDashboard.putNumber("Vision distance boiler", visionBoiler.getDistanceTargetBoiler());
		SmartDashboard.putNumber("Vision angle to boiler", visionBoiler.getAngleTargetHorizontalBoiler());
        
	    SmartDashboard.putNumber("Distance from wall (right,feet): ", Robot.gear.getDistanceFeet());
	    SmartDashboard.putNumber("Distance from wall (right,inches): ", Robot.gear.getDistanceInches());
	    
	    SmartDashboard.putNumber("Shooter speed error", -Robot.shooter.getPIDError());
	    SmartDashboard.putNumber("Elevator speed error", -Robot.elevator.getPIDError());

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

