 
# SteamWorks

## Coding Standards

### Minimum Visibility
- Only make things `public` if required by outside code. Anything internal to a subsystem should be `private`
- Sensors and actuators should be `private` and inside a subsystem. Exceptions to this are PDP and Compressor, which must be declared in `Robot.java`. 
- Nothing should directly read a joystick, joystick axis, or joystick button. All queries for driver input should go through a properly named command in OI. This enables us to more easily monitor our user->robot code, and ensure we can smoothly adjust how the drivers interact with the robot.

### Naming Conventions
- Use `getSomeValue()` and `setSomeValue(value)` if your function is simply adjusting a private variable, or doing a quick action. This ensures consistency and easy matching across our robot. 
- Use descriptive names. The name should describe, briefly, what your code does. 
	- Good examples, which leave you with a documentation-free idea of what is happening
		- `setShooterRPM()`
		- `shooter.setRPM()` is also acceptable, since it's still clear what's being changed.
		- `getTargetDistance()`
		- `driveXFeet`
	- Bad examples: 
		- `shoter.set(value)` What are we setting? What type of input is Value?
		- `get()` What is the meaning of what we get back?
- Boolean values should generally have a `isSomethingTrue()` name pattern. This is consistent with the FRC tools we use.  

### Documentation Comments
These style of comments are very helpful, and eclipse will include them in auto-complete and hover descriptions of your function. 

These are not strictly needed; If your function is well named, what it expects should be obvious. However, if your function does something unusual, or must be used in a specific way, it may be helpful to write this down to help your users. 

Example: 

```java
/**
 * Doc Comment 
 * 
 * @param inches Units to convert from 
 * @return mm  units to conver tto 
 */
public double InchesToMetric(inches){
	return inches*2.54
}
```

### Changes from last year
#### Command Groups vs Commands
We should create a new folder for `ComandGroups`, seperate from Commands. Last year was getting to be a mess, and this would help organize.


#### Requires
We should use `requires()` and `initDefaultCommand()`. This would have actually solved numerous issues with last year's code structure (think the joystick/lifter commands). Note, we need to be aware of how they work: These should only be used when a command is taking direct control in a way that should cause other commands to quit. Simply talking to a subsystem (eg, reading a sensor, getting a sensor, or setting a setpoint) would not necessarily indicate that something should be stopped. 

An example is our Chassis code. `initDefaultCommand()` should point to a `joystickDrive` command. Anything that needs to take control away (such as auto or `sketchydrive`) would require it. This would cause `joystickDrive` to exit. `initDefaultCommand` would restart `joystickDrive` when `sketchydrive` exits, putting us back to our correct state. Considering we'll have numerous commands with both a "default" behaviour and a "triggered" behavior, this is an ideal solution. 

An example of _not_ using requires is a `Shoot` command that asks the Chassis if we've been bumped. This does not need to stop driver control, and should not require the subsystem. 

#### Smaller Subsystems
As part of the previous change, the subsystems shold be a bit smaller to facilitate more precise control of what is requiring what. We wouldn't (for instance) want to `require` our shooter in a way that makes us responsible for the turret, blender, elevator, and shooter wheel unless it's actually necessary. 



## Required Subsystems

### Chassis

#### Sensors/Actuators
- Motors
- Encoders 
- Gyro

#### Functions 
- normal driving functions
- bump detection

- setBearing(cardinal_direction)
- setRelativeBearing(relative_compass_direction)
- hold() //automatic method that fights to maintain current position

### Gear
Responsible for hardware involved with placing and aquiring goal

#### Sensors/Actuators
- UltraSonicLeft
- UltraSonicRight
- Servo 
- GearSwitch 

#### Functions 
- isGearPresent()
- DistanceLeft()
- DistanceRight()
- open()
- close()



### Climber
Responsible for handling climbing operations 

#### Sensors/Actuators
- Motor
- Current Draw

#### Functions 
Uncertain as to specific implimentation. Must be able to help a command step through the climbing process, with the 
assumed states
- Low current: no rope aquired
- Increasing current: Rope aquired, robot is increasing into the air
- Constant current: Robot is off the ground, fully supported by rope. 
- Stall current : Robot has pressed switched and is firmly in place against the switch



### Intake
Responsible for picking balls off the floor, and loading them into the robot. 

#### Sensors/Actuators
Motors

#### Functions
- on()
- off()
- reverse() 
- isStalled()


### Blender
Responsible for shoving balls from the hopper into the elevator

#### Sensors/Actuators
Motor

#### Functions
- on()
- off()
- reverse() 
- isStalled()



### Elevator
Responsible for loading the balls from the blender output and delivering them to the shooter. 

Must ensure that the balls enter the shooter at a reasonably consistent rate to improve
shooter consistency.

We should also closely watch how our loading speed affects shot distance. We may remove 
the option to adjust balls per second in favor of 1 or 2 known good values/tunings

#### Sensors/Actuators
Motor
Quadrature Sensor

#### Functions
- setSpeed(balls_per_second) // Maybe?
- on()
- off()
- reverse() 
- isStalled()




### Turret 
Responsible for turning and maintaining shooter position.

#### Sensors/Actuators
Motor
Quadrature Sensor

#### Functions
- setPosition()
- setRelativePosition()



### Shooter
Responsible for setting and maintaining RPM to shoot our targets.

This should have some sort of dynamic "on the fly" bias configuration in competitions, just in case our shots consistently do something unusual due to motor wear or other unexpected things. 

#### Sensors/Actuators
Motor
Quadrature Sensor

#### Functions
One/all of the following

- setRPM()
- setTargetDistance()
- setBias(bias_amount)


### VisionGear
Responsible for determining placement of the gear placement based on camera

May be merged with Gear if it simplifies code structure (to be determined).

#### Functions
- provide relative bearing
- provide estimated distance (Gear Ultrasonics are also usable) 


### VisionBoiler
- provide relative bearing
- provide distance to target

May be merged with Shooter or Turret if it simplifies code structure (to be determined).

#### Functions
- provide relative bearing to target
- provide distance to target
- provide target lock boolean


### Unassigned

#### Functions
- Convert target distance to desired output RPM of the shooter
- Add robot velocity to output RPM and trajectory calculations