package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeIn extends Command {

    public IntakeIn() {
    
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.intake.intakeIn();
    }

   
    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

 
    protected void interrupted() {
    }
}
