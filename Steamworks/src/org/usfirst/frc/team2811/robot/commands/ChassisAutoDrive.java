package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.subsystems.MiniPID;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ChassisAutoDrive extends Command {
	
	
	private MiniPID minipid;
	private double targetFeet;
	private double targetTicks = 0;
	private Preferences prefs;
	private double p;
	private double i;
	private double d;
	private double maxI;
	private double setPointRange;
	
	
    public ChassisAutoDrive(double feet) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.chassis);
        this.targetFeet = feet;
    	minipid = new MiniPID(0,0,0);
    	prefs = Preferences.getInstance();
    
    }
    
    public ChassisAutoDrive(double feet, double ticks){
    	requires(Robot.chassis);
        this.targetFeet = feet;
        this.targetTicks = ticks;
    	minipid = new MiniPID(0,0,0);
    	prefs = Preferences.getInstance();
    }
    
    private void initMiniPID(){
    	p = prefs.getDouble("DriveFeet PROPORTIONAL", 0);
    	i = prefs.getDouble("DriveFeet INTEGRAL", 0);
    	d = prefs.getDouble("DriveFeet DERIVATIVE", 0);
    	maxI=prefs.getDouble("DriveFeetMaxI", 0);
    	setPointRange = prefs.getDouble("DriveFeetSetpointRange", 0);
    	
    	checkKeys("DriveFeetProportional",p);
    	checkKeys("DriveFeetIntegral",i);
    	checkKeys("DriveFeetDerivative",d);
    	checkKeys("DriveFeetMaxI",maxI);
    	checkKeys("DriveFeetSetpointRange",setPointRange);
    	
    	minipid.setOutputLimits(-1,1);
    	minipid.setSetpointRange(setPointRange);
		minipid.setMaxIOutput(maxI);
		minipid.setPID(p, i, d);
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	minipid.reset();
    	initMiniPID();
    	setTimeout(8);
    	Robot.chassis.autoShiftEnabled = false;
    	Robot.chassis.encoderReset();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	double output = minipid.getOutput(Robot.chassis.getFeet(), targetFeet);
//    	Robot.chassis.drive(output, 0);
    	
		Robot.chassis.drive(-0.7, 0);
		SmartDashboard.putNumber("FeetDriven",Robot.chassis.getFeet());


    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(isTimedOut())cancel();
        return (Robot.chassis.getFeet() >= targetFeet);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.drive(0, 0);
    	Robot.chassis.autoShiftEnabled = true;
    	Robot.chassis.encoderReset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	SmartDashboard.putString("WARNING:", "Command timed out!");
    }
    
    //TODO put in utilities
    private void checkKeys(String key, double value){
		if(!prefs.containsKey(key)) prefs.putDouble(key, value);
	}
}
