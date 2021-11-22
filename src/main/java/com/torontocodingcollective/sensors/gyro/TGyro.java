package com.torontocodingcollective.sensors.gyro;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.kauailabs.navx.frc.AHRS;
import com.torontocodingcollective.TUtil;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * TGyro class is the base class for all TGyros
 * <p>
 * The TGyro class provides a unifying interface for all gyro types and is
 * guaranteed to return a value of 0 <= angle < 360 degrees.
 * <p>
 * The TGyro class supports for clarity the methods of {@link Gyro} except for
 * the {@link #free()} method.
 */
public class TGyro implements Gyro, Sendable {

	public enum TGyroType {

		ANALOG     (false),
		AXDRS450   (false),
		NAV_X      (true),
		PIGEON_IMU (true);

		private final boolean supportsPitch;

		private TGyroType(boolean supportsPitch) {
			this.supportsPitch = supportsPitch;
		}
	};

	/** Default sensitivity is for a VEX analog yaw rate gyro */
	public static final double DEFAULT_ANALOG_GYRO_SENSITIVITY = .00172;

	private final TGyroType gyroType;

	private boolean isInverted;
	private double  offset = 0;

	private AnalogGyro    analogGyro = null;
	private ADXRS450_Gyro adxrs450   = null;
	private PigeonIMU     pigeonIMU  = null;
	private AHRS          navXGyro   = null;

	private double        lastRawAngle = 0;

	public TGyro(TGyroType gyroType, boolean isInverted) {

		this.gyroType      = gyroType;
		this.isInverted    = isInverted;

		switch (gyroType) {

		case AXDRS450:
			this.adxrs450 = new ADXRS450_Gyro();
			break;

		case NAV_X:
			navXGyro = new AHRS(Port.kMXP);
			break;

		default:
			break;
		}
	}

	public TGyro(TGyroType gyroType, TalonSRX talonSRX, boolean isInverted) {

		this.gyroType      = gyroType;
		this.isInverted    = isInverted;

		switch (gyroType) {

		case PIGEON_IMU:
			this.pigeonIMU = new PigeonIMU(talonSRX);
			break;

		default:
			break;
		}
	}

	public TGyro(TGyroType gyroType, int address, boolean isInverted) {

		this.gyroType      = gyroType;
		this.isInverted    = isInverted;

		switch (gyroType) {

		case ANALOG:
			this.analogGyro = new AnalogGyro(address);
			this.analogGyro.setSensitivity(DEFAULT_ANALOG_GYRO_SENSITIVITY);
			break;

		case PIGEON_IMU:
			this.pigeonIMU = new PigeonIMU(address);
			break;

		default:
			break;
		}
	}

	/**
	 * Calibrate the gyro by running the gyro calibration routines.
	 * <p>
	 * NOTE: some gyros can only be calibrated on power up and do not support
	 * subsequent calibration. NOTE: ensure the robot is first turned on while it's
	 * sitting at rest before the competition starts to allow for correct gyro
	 * calibration.
	 */
	@Override
	public void calibrate() {

		switch (gyroType) {

		case ANALOG:
			this.offset = 0;
			analogGyro.calibrate();
			setGyroAngle(0);

		case AXDRS450:
			this.offset = 0;
			adxrs450.calibrate();
			setGyroAngle(0);
			break;

		case NAV_X:
			// The NavX board calibrates on power up.
			// The following line does nothing, but is called
			// in case the NavX spec changes
			navXGyro.calibrate();
			setGyroAngle(0);
			break;

		case PIGEON_IMU:
			// The pigeonIMU calibrates on power up.
			setGyroAngle(0);
			break;

		default:
			break;
		}
	}

	/**
	 * Returns the current angle of the gyro
	 *
	 * @returns angle in the range 0 <= angle <= 360
	 */
	@Override
	public double getAngle() {

		switch (gyroType) {

		case ANALOG:

			// Filter out bad values coming from the
			// gyro. The analog gyros can occasionally
			// return a value that is very much different
			// from the previous reading and different than
			// the steady state from the gyro.
			double rawAngle = analogGyro.getAngle();

			if (Math.abs(rawAngle - lastRawAngle) > 360) {
				// If the gyro jumps by a large number then
				// just assume that it has not moved.
				rawAngle = lastRawAngle;
			}

			lastRawAngle = rawAngle;

			return getAngle(rawAngle);

		case AXDRS450:
			return getAngle(adxrs450.getAngle());

		case NAV_X:
			return getAngle(navXGyro.getAngle());

		case PIGEON_IMU:
			return getAngle(pigeonIMU.getAbsoluteCompassHeading());

		default:
			break;
		}

		return 0;
	};

	/**
	 * Get the angle from the rawAngle
	 *
	 * @param rawAngle
	 * @return normalized angle 0 <= angle < 360 where the inversion of the gyro is
	 *         taken into account
	 */
	private double getAngle(double rawAngle) {

		// Invert before subtracting the offset.
		if (isInverted) {
			rawAngle = -rawAngle;
		}

		return normalizedAngle(rawAngle + offset);
	}

	/**
	 * Return the pitch read off the gyro
	 * <p>
	 * Not all gyros allow for multiple axis and this routine will return 0 always
	 * if the pitch is not supported.
	 *
	 * @return pitch in degrees or 0 if pitch is not supported
	 */
	public double getPitch() {

		if (!supportsPitch()) {
			return 0.0d;
		}

		switch (gyroType) {

		case NAV_X:
			// Not sure why pitch and roll are reversed on the NavX.
			// FIXME: Test the pitch every year.
			return navXGyro.getRoll();

		case PIGEON_IMU:
			double[] yawPitchRole = new double[3];
			com.ctre.phoenix.ErrorCode errCd = pigeonIMU.getYawPitchRoll(yawPitchRole);

			if (errCd != com.ctre.phoenix.ErrorCode.OK) {
				System.out.println("Error getting Pitch angle from Pigeon IMU (" +
						errCd + ")");
				return 0.0d;
			}
			// Pitch is the second value in the array.
			return yawPitchRole[1];

		default:
			return 0.0d;
		}
	}

	/**
	 * Set the sensitivity of the analog gyro
	 * <p>
	 * NOTE: This routine is only valid for Analog Gyros.
	 * @param voltsPerDegreePerSecond
	 *            sensitivity of the gyro
	 */
	public void setSensitivity(double voltsPerDegreePerSecond) {

		switch (gyroType) {
		case ANALOG:
			analogGyro.setSensitivity(voltsPerDegreePerSecond);
			break;

		default:
			break;
		}
	}

	/**
	 * Return the rate of change of the angle
	 * <p>
	 * This value is in degrees/sec
	 *
	 * @returns double degrees/sec
	 */
	@Override
	public double getRate() {

		switch (gyroType) {

		case ANALOG:
			return getRate(analogGyro.getRate());

		case AXDRS450:
			return getRate(adxrs450.getRate());

		case NAV_X:
			return getRate(navXGyro.getRate());

		case PIGEON_IMU:
			// Rate is not supported on the Pigeon IMU
			return 0;

		default:
			return 0;
		}
	}

	/**
	 * Get the gyro rate from the rawRate
	 *
	 * @param rawRate
	 * @return rate normalized for inversion
	 */
	private double getRate(double rawRate) {

		if (isInverted) {
			return -rawRate;
		}

		return rawRate;
	}

	/**
	 * Returns whether or not this gyro is inverted
	 *
	 * @return boolean {@code true} if the gyro is inverted, {@code false} otherwise
	 */
	public boolean isInverted() {
		return isInverted;
	}

	/**
	 * Get the angle normalized to a value between 0 and 360 degrees
	 *
	 * @param rawAngle
	 *            value
	 * @return normalized angle value
	 */
	private double normalizedAngle(double rawAngle) {

		double angle = rawAngle % 360.0;

		if (angle < 0) {
			angle += 360.0;
		}

		// Round the angle to 3 decimal places
		return TUtil.round(angle, 3);
	}

	/**
	 * Reset the gyro angle to zero.
	 * <p>
	 * NOTE: This routine never performs a calibration of the gyro, so the amount of
	 * drift will not change. In order to calibrate the gyro, use the
	 * {@link #calibrate()} routine.
	 */
	@Override
	public void reset() {
		setGyroAngle(0);
	}

	public void setGyroAngle(double angle) {

		// clear the previous offset
		offset = 0;

		// set the offset to the current angle
		// in order to zero the output.
		offset = -getAngle();

		// This offset will result in an output
		// of zero. Add the passed in angle
		// to make the desired angle
		offset += angle;
	}

	/**
	 * Indicates whether this gyro supports pitch
	 *
	 * @return boolean {@code true} indicates that pitch is supported {@code false}
	 *         indicates pitch is not supported
	 */
	public boolean supportsPitch() {
		return this.gyroType.supportsPitch;
	}

	@Override
	public void close() throws Exception {

		switch (gyroType) {

		case ANALOG:
			analogGyro.close();
			break;

		case AXDRS450:
			adxrs450.close();
			break;

		case NAV_X:
			// Close is not supported on the NavX
			break;

		case PIGEON_IMU:
			pigeonIMU.DestroyObject();
			break;
		}
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("Gyro");
		builder.addDoubleProperty("Value", this::getAngle, null);
	}

}
