package com.torontocodingcollective.commands.drive;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;
import com.torontocodingcollective.subsystem.TDriveSubsystem;

/**
 * Drive Time Command
 * <p>
 * This command will drive straight (no gyro compensation) for the specified
 * amount of time.
 * <p>
 * The command can be extended to provide additional stopping variations
 * (distance, sensor, etc).
 */
public class TDriveTimeCommand extends TSafeCommand {

	private static final String COMMAND_NAME =
			TDriveTimeCommand.class.getSimpleName();

	private double                speed;
	private final boolean         brakeWhenFinished;

	private final TDriveSubsystem driveSubsystem;

	/**
	 * DriveTimeCommand
	 *
	 * @param speed
	 *            at which to drive in the range -1.0 <= speed <= 1.0. if the speed
	 *            is set to a very small value, the robot will not drive and the
	 *            command will end on the timeout.
	 * @param timeout
	 *            the time after which this command will end automatically. A value
	 *            of {@link TConst#NO_COMMAND_TIMEOUT} will be used as an infinite
	 *            timeout.
	 * @param driveSubsystem
	 *            that extends the {@link TDriveSubsystem}
	 */
	public TDriveTimeCommand(double speed, double timeout, TDriveSubsystem driveSubsystem) {
		this(speed, timeout, TConst.BRAKE_WHEN_FINISHED, driveSubsystem);
	}

	/**
	 * DriveTimeCommand
	 *
	 * @param speed
	 *            at which to drive in the range -1.0 <= speed <= 1.0. if the speed
	 *            is set to a very small value, the robot will not drive and the
	 *            command will end on the timeout.
	 * @param timeout
	 *            the time after which this command will end automatically. A value
	 *            of {@link TConst#NO_COMMAND_TIMEOUT} will be used as an infinite
	 *            timeout.
	 * @param brakeWhenFinished
	 *            {@code true} to brake when the command finishes {@code false} to
	 *            coast into the next command.
	 * @param driveSubsystem
	 *            that extends the {@link TDriveSubsystem}
	 */
	public TDriveTimeCommand(double speed, double timeout, boolean brakeWhenFinished,
			TDriveSubsystem driveSubsystem) {

		super(timeout);

		this.driveSubsystem = driveSubsystem;

		addRequirements(driveSubsystem);

		setSpeed(speed);

		this.brakeWhenFinished = brakeWhenFinished;
	}

	@Override
	protected String getCommandName() { return COMMAND_NAME; }

	@Override
	protected String getParmDesc() {
		return "speed " + this.speed
				+ ", brakeWhenFinished " + this.brakeWhenFinished
				+ ", " + super.getParmDesc();
	}

	@Override
	public void initialize() {

		// Only print the command start message
		// if this command was not subclassed
		if (getCommandName().equals(COMMAND_NAME)) {
			logMessage(getParmDesc() + " starting");
		}

		driveSubsystem.setSpeed(speed, speed);
	}

	@Override
	public void execute() {

		// Update the speed and direction each loop.
		// If the values have not changed, this call will
		// have no effect. If the speed is changing for
		// acceleration or deceleration purposes, then
		// this call will adjust the speed setpoint.
		driveSubsystem.setSpeed(speed, speed);
	}

	/**
	 * Adjust the speed on the driveOnHeading command
	 * <p>
	 * This routine is used to adjust the speed in the drive on heading command
	 * without changing the heading.
	 * <p>
	 * This routine could be used to support acceleration and deceleration when
	 * driving on a heading.
	 *
	 * @param speed
	 *            the speed to drive at when tracking the heading. The speed should
	 *            be between 0 and 1.0. Negative speeds should not be used. If a
	 *            value is given outside this range, then the value will be
	 *            normalized to be within the range
	 */
	public void setSpeed(double speed) {
		this.speed = Math.min(1.0, Math.max(speed, 0));
	}

	@Override
	public boolean isFinished() {

		// Check for a timeout or cancel
		if (super.isFinished()) {
			return true;
		}

		return false;
	}

	@Override
	public void end(boolean interrupted) {

		super.end(interrupted);

		if (brakeWhenFinished) {
			driveSubsystem.setSpeed(0, 0);
		}
	}

}
