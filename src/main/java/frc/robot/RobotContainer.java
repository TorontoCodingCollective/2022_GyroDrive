// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.drive.DefaultDriveCommand;
import frc.robot.commands.pneumatics.DefaultPneumaticsCommand;
import frc.robot.oi.OI;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.subsystems.PowerSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

	// The robot's subsystems
	private final DriveSubsystem     driveSubsystem        = new DriveSubsystem();
	private final PneumaticsSubsystem   pneumaticsSubsystem   = new PneumaticsSubsystem();
	private final PowerSubsystem        powerSubsystem        = new PowerSubsystem();
	private final CameraSubsystem       cameraSubsystem       = new CameraSubsystem();
	private final ControlPanelSubsystem controlPanelSubsystem = new ControlPanelSubsystem();

	// FIXME: is the oi class needed?
	private final OI oi;

	private final Trigger cancelTrigger;

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer(OI oi) {

		this.oi = oi;

		cancelTrigger = new Trigger() {
			@Override
			public boolean get() {
				return oi.getCancel();
			}
		};

		// Configure the button bindings
		configureButtonBindings();

		// Configure default commands for each subsystem.
		// Setting a default command automatically registers the subsystem.
		driveSubsystem     .setDefaultCommand(new DefaultDriveCommand(oi, cancelTrigger, driveSubsystem));
		pneumaticsSubsystem.setDefaultCommand(new DefaultPneumaticsCommand(oi, cancelTrigger, pneumaticsSubsystem));

		// If a subsystem does not have a default command, then register
		// that subsystem with the CommandScheduler in order to have the periodic()
		// method called
		// each loop.
		CommandScheduler.getInstance().registerSubsystem(powerSubsystem);
		CommandScheduler.getInstance().registerSubsystem(cameraSubsystem);
		CommandScheduler.getInstance().registerSubsystem(controlPanelSubsystem);

		// The OI layer periodic method should also be called.
		CommandScheduler.getInstance().registerSubsystem(oi);
	}

	/**
	 * Use this method to define your button->command mappings. Buttons can be
	 * created by instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of
	 * its subclasses ({@link edu.wpi.first.wpilibj.Joystick} or
	 * {@link PS4Controller}), and then passing it to a
	 * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	 */
	private void configureButtonBindings() {
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// no auto
		return new AutonomousCommand(cancelTrigger, driveSubsystem);
	}

	public void autonomousInit() {

		// Turn on the drive pids for auto
		oi.setSpeedPidEnabled(true);
		driveSubsystem.enableSpeedPids();

		// Reset the gyro and the encoders
		driveSubsystem.setGyroAngle(0);
		driveSubsystem.resetEncoders();

	}
}
