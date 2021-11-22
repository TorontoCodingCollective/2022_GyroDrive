package frc.robot;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.motorcontroller.TMotorController.TMotorControllerType;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * <p>
 * This map is intended to define the wiring only. Robot constants should be put
 * in {@link RobotConst}
 */
public class RobotMap {

	// ******************************************
	// Speed Controllers and encoders
	// CAN addresses
	// ******************************************
	public static final int                     LEFT_DRIVE_MOTOR_CONTROLLER_ADDRESS;
	public static final TMotorControllerType    LEFT_DRIVE_MOTOR_CONTROLLER_TYPE;
	public static final int                     LEFT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS;
	public static final TMotorControllerType    LEFT_DRIVE_FOLLOWER_MOTOR_TYPE;
	public static final boolean                 LEFT_DRIVE_MOTOR_ISINVERTED;

	public static final int                     RIGHT_DRIVE_MOTOR_CONTROLLER_ADDRESS;
	public static final TMotorControllerType 	RIGHT_DRIVE_MOTOR_CONTROLLER_TYPE;
	public static final int                     RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS;
	public static final TMotorControllerType    RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_TYPE;
	public static final boolean                 RIGHT_DRIVE_MOTOR_ISINVERTED;

	public static final boolean                 LEFT_DRIVE_ENCODER_ISINVERTED;
	public static final boolean                 RIGHT_DRIVE_ENCODER_ISINVERTED;

	// ******************************************
	// Gyro Ports
	// ******************************************
	public static final int                     GYRO_PORT;
	public static final boolean                 GYRO_ISINVERTED;

	// ******************************************
	// Pneumatics Ports
	// ******************************************
	public static final int                     SHIFTER_PNEUMATIC_PORT = 0;

	// Initializers if this code will be deployed to more than one
	// robot with different mappings
	static {

		switch (RobotConst.robot) {

		case RobotConst.TEST_ROBOT:
		default:
			// CAN Constants
			// Talon and Victor connected through the CAN Bus
			LEFT_DRIVE_MOTOR_CONTROLLER_ADDRESS           = 10;
			LEFT_DRIVE_MOTOR_CONTROLLER_TYPE              = TMotorControllerType.TALON_SRX_CAN;
			LEFT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS  = 11;
			LEFT_DRIVE_FOLLOWER_MOTOR_TYPE                = TMotorControllerType.VICTOR_SPX_CAN;
			LEFT_DRIVE_MOTOR_ISINVERTED                   = TConst.INVERTED;
			LEFT_DRIVE_ENCODER_ISINVERTED                 = TConst.INVERTED;

			RIGHT_DRIVE_MOTOR_CONTROLLER_ADDRESS          = 20;
			RIGHT_DRIVE_MOTOR_CONTROLLER_TYPE             = TMotorControllerType.TALON_SRX_CAN;
			RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_ADDRESS = 21;
			RIGHT_DRIVE_FOLLOWER_MOTOR_CONTROLLER_TYPE    = TMotorControllerType.TALON_SRX_CAN;
			RIGHT_DRIVE_MOTOR_ISINVERTED                  = TConst.NOT_INVERTED;
			RIGHT_DRIVE_ENCODER_ISINVERTED                = TConst.NOT_INVERTED;

			GYRO_PORT       = 0;
			GYRO_ISINVERTED = TConst.NOT_INVERTED;
		}
	}
}
