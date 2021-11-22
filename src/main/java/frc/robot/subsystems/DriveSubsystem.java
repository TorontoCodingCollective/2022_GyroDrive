package frc.robot.subsystems;

import com.torontocodingcollective.motorcontroller.TMotorController;
import com.torontocodingcollective.sensors.encoder.TEncoder;
import com.torontocodingcollective.sensors.gyro.TAnalogGyro;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotConst;
import frc.robot.RobotMap;

/**
 * Chassis Subsystem
 * <p>
 * This class is describes all of the components in a differential (left/right)
 * drive subsystem.
 */
public class DriveSubsystem extends TGyroDriveSubsystem {

	private static final boolean LOW_GEAR     = false;
	private static final boolean HIGH_GEAR    = true;

	private Solenoid             shifter      = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SHIFTER_PNEUMATIC_PORT);
	private boolean              turboEnabled = false;

	public DriveSubsystem() {

		super(
				// Left Speed Controller
				new TMotorController(
						RobotMap.LEFT_DRIVE_MOTOR_CONTROLLER_TYPE,
						RobotMap.LEFT_DRIVE_MOTOR_CONTROLLER_ADDRESS,
						RobotMap.LEFT_DRIVE_FOLLOWER_MOTOR_TYPE,
						RobotMap.LEFT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS,
						RobotMap.LEFT_DRIVE_MOTOR_ISINVERTED),

				// Right Speed Controller
				new TMotorController(
						RobotMap.RIGHT_DRIVE_MOTOR_CONTROLLER_TYPE,
						RobotMap.RIGHT_DRIVE_MOTOR_CONTROLLER_ADDRESS,
						RobotMap.RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_TYPE,
						RobotMap.RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS,
						RobotMap.RIGHT_DRIVE_MOTOR_ISINVERTED),

				// Gyro used for this subsystem
				new TAnalogGyro(RobotMap.GYRO_PORT, RobotMap.GYRO_ISINVERTED),

				// Gyro PID Constants
				RobotConst.DRIVE_GYRO_PID_KP,
				RobotConst.DRIVE_GYRO_PID_KI,
				RobotConst.DRIVE_MAX_ROTATION_OUTPUT);

		// Get the encoders attached to the CAN bus speed controllers
		TEncoder leftEncoder = getSpeedController(TSide.LEFT).getEncoder();
		TEncoder rightEncoder = getSpeedController(TSide.RIGHT).getEncoder();

		super.setEncoders(
				leftEncoder,  RobotMap.LEFT_DRIVE_MOTOR_ISINVERTED,
				rightEncoder, RobotMap.RIGHT_DRIVE_MOTOR_ISINVERTED,
				RobotConst.ENCODER_COUNTS_PER_INCH,
				RobotConst.DRIVE_SPEED_PID_KP,
				RobotConst.DRIVE_SPEED_PID_KI,
				RobotConst.MAX_LOW_GEAR_SPEED);
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
		setMaxEncoderSpeed(RobotConst.MAX_HIGH_GEAR_SPEED);
		shifter.set(HIGH_GEAR);
	}

	public void disableTurbo() {
		turboEnabled = false;
		setMaxEncoderSpeed(RobotConst.MAX_LOW_GEAR_SPEED);
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
