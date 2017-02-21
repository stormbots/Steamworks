package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.subsystems.MiniPID;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ChassisAutoTurn extends Command {
	
private Preferences prefs;
	
	private MiniPID minipid = new MiniPID(0,0,0);
	private double targetAngle;
	private double toleranceAngle;
	private double output=0;
	private double turnStableMotorPower;
	private double minimumTurnSpeed;
	private double valueDeadbend;
	private double pidout;
	private double p;
	private double i;
	private double d;
	private double maxI;
	private double range;
	
	
	private void initPID(){
		 p=prefs.getDouble("TurnP", 0);
		 i=prefs.getDouble("TurnI", 0);
		 d=prefs.getDouble("TurnD", 0);
		 maxI=prefs.getDouble("TurnMaxI", 0);
		 range=prefs.getDouble("TurnOutputRange", 10);
		 turnStableMotorPower=prefs.getDouble("turnStableMotorPower", .2);
		 valueDeadbend = prefs.getDouble("valueDeadbend", 0);
		
		 checkKeys("TurnP",p);
		 checkKeys("TurnI",i);
		 checkKeys("TurnD",d);
		 checkKeys("TurnP",maxI);
		 checkKeys("TurnP",range);
		 checkKeys("turnStableMotorPower",turnStableMotorPower);
		 checkKeys("valueDeadbend",valueDeadbend);
		 
		
		 minipid.setOutputLimits(-.3,.3);
		 minipid.setSetpointRange(range);	
		 minipid.setMaxIOutput(maxI);
		 minipid.setPID(p, i, d);
	}
	

    public ChassisAutoTurn(double targetAngle, double toleranceAngle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.chassis);
    	this.targetAngle = targetAngle;
    	this.toleranceAngle = toleranceAngle;
    	prefs = Preferences.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	this.minimumTurnSpeed = prefs.getDouble("minimumTurnSpeed", 0);
    	minipid.reset();
    	initPID();
    	minipid.setOutputLimits(1-minimumTurnSpeed);
    	setTimeout(8);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	
    	Robot.chassis.drive(0, .7);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    private void checkKeys(String key, double value){
		if(!prefs.containsKey(key)) prefs.putDouble(key, value);
	}
}
