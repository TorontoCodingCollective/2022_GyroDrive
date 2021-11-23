package frc.robot.subsystems;

import com.torontocodingcollective.motorcontroller.TMotorController;
import com.torontocodingcollective.sensors.encoder.TEncoder;
import com.torontocodingcollective.sensors.gyro.TGyro;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;

/**
 * Chassis Subsystem
 * <p>
 * This class is describes all of the components in a differential (left/right)
 * drive subsystem.
 */
public class DriveSubsystem extends TGyroDriveSubsystem {

	private static final boolean LOW_GEAR     = false;
	private static final boolean HIGH_GEAR    = true;

	private Solenoid             shifter      = new Solenoid(PneumaticsModuleType.CTREPCM, DriveConstants.SHIFTER_PNEUMATIC_PORT);
	private boolean              turboEnabled = false;

	public DriveSubsystem() {

		super(
				// Left Speed Controller
				new TMotorController(
						DriveConstants.LEFT_DRIVE_MOTOR_CONTROLLER_TYPE,
						DriveConstants.LEFT_DRIVE_MOTOR_CONTROLLER_ADDRESS,
						DriveConstants.LEFT_DRIVE_FOLLOWER_MOTOR_TYPE,
						DriveConstants.LEFT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS,
						DriveConstants.LEFT_DRIVE_MOTOR_ISINVERTED),

				// Right Speed Controller
				new TMotorController(
						DriveConstants.RIGHT_DRIVE_MOTOR_CONTROLLER_TYPE,
						DriveConstants.RIGHT_DRIVE_MOTOR_CONTROLLER_ADDRESS,
						DriveConstants.RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_TYPE,
						DriveConstants.RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS,
						DriveConstants.RIGHT_DRIVE_MOTOR_ISINVERTED),

				// Gyro used for this subsystem
				new TGyro(DriveConstants.GYRO_TYPE, DriveConstants.GYRO_PORT, DriveConstants.GYRO_ISINVERTED));

		/*
		 * Get the encoders attached to the CAN bus speed controllers
		 * NOTE: Depending on the encoder type, and where it is attached, different
		 *       constructors would be required to make a TEncoder
		 *       For DIO port wired speed controllers use
		 *       TEncoder encoder = new TEncoder(dioChannelA, dioChannelB, isInverted);
		 */
		TEncoder leftEncoder  = getSpeedController(TSide.LEFT).getEncoder();
		TEncoder rightEncoder = getSpeedController(TSide.RIGHT).getEncoder();

		// Set up the encoders
		super.setEncoders(
				leftEncoder,  DriveConstants.LEFT_DRIVE_MOTOR_ISINVERTED,
				rightEncoder, DriveConstants.RIGHT_DRIVE_MOTOR_ISINVERTED,
				DriveConstants.ENCODER_COUNTS_PER_INCH);

		// Set up the drive speed pids
		super.setSpeedPid(
				DriveConstants.DRIVE_SPEED_PID_KP,
				DriveConstants.DRIVE_SPEED_PID_KI,
				DriveConstants.MAX_LOW_GEAR_SPEED);

		// Set up the gyro tracking pids
		super.setGyroPidGain(
				DriveConstants.DRIVE_GYRO_PID_KP,
				DriveConstants.DRIVE_GYRO_PID_KI);

		// Set the max output speed used on in place pivot rotations
		super.setMaxRotationOutput(DriveConstants.DRIVE_MAX_ROTATION_OUTPUT);

	}

	@Override
	public void init() {
		shifter.set(LOW_GEAR);
	}


	// ********************************************************************************************************************
	// Turbo routines
	// ********************************************************************************************************************
	public void enableTurbo() {
		turboEnabled = true;
		setMaxEncoderSpeed(DriveConstants.MAX_HIGH_GEAR_SPEED);
		shifter.set(HIGH_GEAR);
	}

	public void disableTurbo() {
		turboEnabled = false;
		setMaxEncoderSpeed(DriveConstants.MAX_LOW_GEAR_SPEED);
		shifter.set(LOW_GEAR);
	}

	public boolean isTurboEnabled() {
		return turboEnabled;
	}

	@Override
	public void periodic() {

		super.periodic();

		SmartDashboard.putBoolean("Turbo Enabled", isTurboEnabled());
	}

}
