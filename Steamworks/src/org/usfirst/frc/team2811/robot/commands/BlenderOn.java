package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BlenderOn extends Command {

    private double last;
    private boolean blenderOn;

	public BlenderOn() {
        requires(Robot.blender);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	last = Timer.getFPGATimestamp();
    	blenderOn=true;

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if(Robot.blender.isBlenderOn()&&Timer.getFPGATimestamp() - last > 1.5){
    		blenderOn = !blenderOn;
    		last=Timer.getFPGATimestamp();
    		System.out.println("Blender set to Off " + last);
    	}else if(!Robot.blender.isBlenderOn()&&Timer.getFPGATimestamp() - last >0.1){
    		blenderOn = !blenderOn;
    		last=Timer.getFPGATimestamp(); 
   		System.out.println("Blender set to On " + last);
    	}
    	
//    	if(Robot.blender.isBlenderOn()&&Robot.blender.isBlenderStalled()){
//    		blenderOn = !blenderOn;
//    		last=Timer.getFPGATimestamp();
//    		System.out.println("Blender set to Off " + last);
//    	}else if(!Robot.blender.isBlenderOn()&&Timer.getFPGATimestamp() - last >0.75){
//    		blenderOn = !blenderOn;
//    		last=Timer.getFPGATimestamp(); 
//    		System.out.println("Blender set to On " + last);
//    	}
    	
    	//toggle motor in the target direction
    	if(blenderOn){
    		//TODO rename blender off to backwards since it goes backwards, instead of off
    		Robot.blender.setBlenderOn();
    	}
    	else{
    		Robot.blender.setBlenderReverse();
    	}
    	
		//Robot.blender.setBlenderOn();

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
}
