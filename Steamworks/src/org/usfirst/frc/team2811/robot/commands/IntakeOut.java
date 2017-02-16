package org.usfirst.frc.team2811.robot.commands;

import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeOut extends Command {

    public IntakeOut() {
    	requires(Robot.intake);
        
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.intake.intakeOut();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

   
    protected void interrupted() {
    }
}
