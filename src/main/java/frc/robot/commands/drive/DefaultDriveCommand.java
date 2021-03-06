package frc.robot.commands.drive;

import com.torontocodingcollective.TDifferentialDrive;
import com.torontocodingcollective.TSpeeds;
import com.torontocodingcollective.commands.TDefaultDriveCommand;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;

import frc.robot.oi.OI;
import frc.robot.subsystems.DriveSubsystem;

/**
 * Default drive command for a drive base
 */
public class DefaultDriveCommand extends TDefaultDriveCommand {

	private static final String COMMAND_NAME =
			DefaultDriveCommand.class.getSimpleName();

	private final OI                oi;
	private final DriveSubsystem    driveSubsystem;

	TDifferentialDrive differentialDrive = new TDifferentialDrive();

	public DefaultDriveCommand(OI oi, DriveSubsystem driveSubsystem) {

		// The drive logic will be handled by the TDefaultDriveCommand
		// which also contains the requires(driveSubsystem) statement
		super(oi, driveSubsystem);

		this.oi = oi;
		this.driveSubsystem = driveSubsystem;

	}

	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	protected String getParmDesc() {
		return super.getParmDesc();
	}

	// Called just before this Command runs the first time
	@Override
	public void initialize() {

		// Print the command parameters if this is the current
		// called command (it was not sub-classed)
		if (getCommandName().equals(COMMAND_NAME)) {
			logMessage(getParmDesc() + " starting");
		}

		super.initialize();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {

		// Check the driver controller buttons
		super.execute();

		// Enable turbo mode
		if (oi.getTurboOn()) {
			driveSubsystem.enableTurbo();
		} else {
			driveSubsystem.disableTurbo();
		}

		// Drive according to the type of drive selected in the
		// operator input.
		TStickPosition leftStickPosition = oi.getDriveStickPosition(TStick.LEFT);
		TStickPosition rightStickPosition = oi.getDriveStickPosition(TStick.RIGHT);

		TStick singleStickSide = oi.getSelectedSingleStickSide();

		TSpeeds motorSpeeds;

		switch (oi.getSelectedDriveType()) {

		case SINGLE_STICK:
			TStickPosition singleStickPosition = rightStickPosition;
			if (singleStickSide == TStick.LEFT) {
				singleStickPosition = leftStickPosition;
			}
			motorSpeeds = differentialDrive.arcadeDrive(singleStickPosition);
			break;

		case TANK:
			motorSpeeds = differentialDrive.tankDrive(leftStickPosition, rightStickPosition);
			break;

		case ARCADE:
		default:
			motorSpeeds = differentialDrive.arcadeDrive(leftStickPosition, rightStickPosition);
			break;
		}

		driveSubsystem.setSpeed(motorSpeeds);
	}

	@Override
	public boolean isFinished() {
		// The default command does not end
		return false;
	}
}
