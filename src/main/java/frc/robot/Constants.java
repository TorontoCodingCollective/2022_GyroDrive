package frc.robot;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.motorcontroller.TMotorController.TMotorControllerType;
import com.torontocodingcollective.sensors.gyro.TGyro.TGyroType;

public final class Constants {

	public static final String  TEST_ROBOT                    = "TestRobot";
	public static final String  PROD_ROBOT                    = "ProdRobot";

	// The TorontoCodingCollective framework was developed to run on different
	// robots through the use of multiple mappings and constants.
	public static final String robot = TEST_ROBOT;

	public static enum Direction {
		FORWARD, BACKWARD
	};

	public static final class DriveConstants {

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

		public static final double                  ENCODER_COUNTS_PER_INCH;

		// ******************************************
		// Gyro Ports
		// ******************************************
		public static final TGyroType               GYRO_TYPE;
		public static final int                     GYRO_PORT;
		public static final boolean                 GYRO_ISINVERTED;

		// ******************************************
		// Pneumatics Ports
		// ******************************************
		public static final int                     SHIFTER_PNEUMATIC_PORT = 0;

		// *********************************************************
		// PID Constants
		// *********************************************************
		public static final double                 MAX_LOW_GEAR_SPEED;
		public static final double                 MAX_HIGH_GEAR_SPEED;

		public static final double                 DRIVE_GYRO_PID_KP;
		public static final double                 DRIVE_GYRO_PID_KI;
		public static final double                 DRIVE_MAX_ROTATION_OUTPUT     = 0.6;

		public static final double                 DRIVE_SPEED_PID_KP;
		public static final double                 DRIVE_SPEED_PID_KI;

		// *********************************************************
		// Ultrasonic Sensor Calibration
		// *********************************************************
		public static final double                 ULTRASONIC_VOLTAGE_20IN       = 0.191;
		public static final double                 ULTRASONIC_VOLTAGE_40IN       = 0.383;
		public static final double                 ULTRASONIC_VOLTAGE_80IN       = 0.764;

		static {

			switch (robot) {

			case TEST_ROBOT:
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

				GYRO_TYPE       = TGyroType.ANALOG;
				GYRO_PORT       = 0;
				GYRO_ISINVERTED = TConst.NOT_INVERTED;

				// The low gear speed should be set just below the
				// maximum loaded speed of the robot
				MAX_LOW_GEAR_SPEED = 320.0; // Encoder counts/sec
				MAX_HIGH_GEAR_SPEED = 900.0;

				// Typically set the integral gain at 1/20 of the
				// proportional gain.  The gain can often be increased
				// above this value, but typically gives good
				// stability and acceptable performance
				DRIVE_GYRO_PID_KP = .07;
				DRIVE_GYRO_PID_KI = DRIVE_GYRO_PID_KP / 20.0;

				DRIVE_SPEED_PID_KP = 0.4;
				DRIVE_SPEED_PID_KI = DRIVE_SPEED_PID_KP / 20.0;

				ENCODER_COUNTS_PER_INCH = 55.6;

				break;
			}

		}
	}
}


