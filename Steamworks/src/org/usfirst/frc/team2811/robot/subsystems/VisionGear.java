package org.usfirst.frc.team2811.robot.subsystems;

import org.usfirst.frc.team2811.robot.Util;
import org.usfirst.frc.team2811.robot.commands.VisionBoilerUpdate;
import org.usfirst.frc.team2811.robot.commands.VisionGearUpdate;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionGear extends Subsystem {

	private NetworkTable networkTable;
	private Preferences prefs;
	private double distanceTarget = 0;	
	private double angleTargetHorizontal = 0;
	private double visionTimestamp = 0;
	private double robotTimestamp = 0;
	private boolean isEnabled;
	private double visionValidTargetTimeout;
	private int lastHeartbeat = -1;
	private long lastHeartbeatTime = 0;
	private int numNtFaults = 0;	
	private double angleAdjust;
	
	private double[][] calibrationMapping24inch={
			//measured from off-center from the gear
			//{Reported, actual target turn value}
			{4.9,	14.55},	//-6"
			{5.05,	11.57},	//-3
			{9.29,	0.0},	//0"
			{18.6,	-13},	//+3"
			{24.79,	-16},	//+6"
		};
	
//	private double[][] calibrationMapping24inch={
//			//measured from off-center from the gear
//			//{Reported, actual target turn value}
//			{-29,	-11},	//-6"
//			{-7.05,	-5},	//-3
//			{2.83,	0.0},	//0"
//			{9.09,	22},	//+3"
//			{18.6,	33},	//+6"
//		};
	
	private double[][] calibrationMapping48inch={
			//measured from off-center from the gear
			{-5.7,	-9},	//-12
			{-8,	-6},	//-6"
			{0.0,	0.0},	//0" //TODO WE NEED TO VERIFY THIS
			{16.6,	16},	//+6"
			{28.75,	26},	//+12"
		};
	
    public VisionGear() {
    	networkTable=NetworkTable.getTable("vision");
    	prefs = Preferences.getInstance();
    	visionValidTargetTimeout=0;
    	
    	// make sure some preference values get set so that the pi
    	// picks them up relatively quickly
    	Util.getPreferencesDouble("VisionGearRectTolerance", 0.2);
    	Util.getPreferencesDouble("VisionGearRectToleranceLow", 1.9);
    	Util.getPreferencesDouble("VisionGearRectToleranceHigh", 3);
    	Util.getPreferencesDouble("VisionGearFudgeFactor", 0.85);
    	Util.getPreferencesDouble("VisionGearForwardPixel", 614);
	}

    public void updateValFromFlash(){
    	visionValidTargetTimeout=Util.getPreferencesDouble("visionValidTargetTimeout",0.1);
    	angleAdjust = Util.getPreferencesDouble("Vision Gear Angle Adjustment", 2.0);
    }
   

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new VisionGearUpdate());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	/**
	 * Check for distance calibration at 24" away from the target
	 * @return target angle to turn to
	 */

	public double getAngleHorizontal(){
		return getAngleHorizontalAtDistance(24);
	}
	
	/**
	 * Check the best dataset for the calibrated distance, return our target angle
	 * @param inches away from the gear target (using ultrasonics)
	 * @return target angle to turn to
	 */
	public double getAngleHorizontalAtDistance(double inches){
		if(inches>=48) return Util.getMapValueFromList(angleTargetHorizontal,calibrationMapping48inch);
		else if(inches>=24) return Util.getMapValueFromList(angleTargetHorizontal,calibrationMapping24inch);
		else return Util.getMapValueFromList(angleTargetHorizontal,calibrationMapping24inch);
	}
	
	public void enable(){
		networkTable.putBoolean("enabled", true);
	}
	
	public void disable(){
		networkTable.putBoolean("enabled", false);
	}
	
	public boolean haveValidTargetGear(){
		double now = Timer.getFPGATimestamp();
		
		// See how old our robot data is
		// If it's real old, assume it's invalid
		
		if (now-robotTimestamp<visionValidTargetTimeout){
			SmartDashboard.putNumber("TimeStamp", now-robotTimestamp);
			return true;
		}else{
			return false;
		}
		
	}
	
	public void update(){
		//connect to network tables
		//update our internal values
		//should be run constantly from a defaultcommand
		double time=networkTable.getNumber("gear_frame_number",0);

		if(visionTimestamp!=time){
			//have new data!
			///distanceTarget=networkTable.getNumber("distanceTarget", 0);
			angleTargetHorizontal = networkTable.getNumber("gear_error_angle",0);
			visionTimestamp = time;
			robotTimestamp=Timer.getFPGATimestamp();
		}
	}
	
	public void heartbeat() {
		NetworkTable sdTable = NetworkTable.getTable("SmartDashboard");
		
		int prev = (int) sdTable.getNumber("rio_heartbeat", -1.0);
			
		// first: increment our counter
		sdTable.putNumber("rio_heartbeat", prev + 1);
		
		int piHeartbeat = (int) sdTable.getNumber("raspi_heartbeat", -1.0);
		
		if (piHeartbeat > this.lastHeartbeat) {
			// looks like our connection is still good
			if (this.numNtFaults > 0) {
				this.numNtFaults = 0;
				this.enable();
			}
			this.lastHeartbeatTime = System.currentTimeMillis();
		} else if (piHeartbeat == this.lastHeartbeat) {
			// something may be wrong or we may just be checking too often
			this.numNtFaults++;
		} else {
			// something's almost certainly wrong, since the pi's heartbeat counter decreased
			this.numNtFaults++;
			
			// reset the pi counter since it seems like it potentially came back online
			// and, at any rate, if it increases from here, everything is okay
			this.lastHeartbeat = piHeartbeat;
		}
		
		if (System.currentTimeMillis() - 1000 > this.lastHeartbeatTime) {
			// something's seriously wrong here
			System.err.println("[VISION]: Raspi heartbeat gone for > 1 second... Disabling.");
			System.err.println("\t---> NetworkTables fault count: " + this.numNtFaults);
			this.disable();
		}
		
		this.lastHeartbeat = piHeartbeat;
	}
	
	public void updateDashboard(){
		SmartDashboard.putNumber("Vision Gear Angle", getAngleHorizontal());
		SmartDashboard.putNumber("Angle from map", getAngleHorizontalAtDistance(24));
	}
}

