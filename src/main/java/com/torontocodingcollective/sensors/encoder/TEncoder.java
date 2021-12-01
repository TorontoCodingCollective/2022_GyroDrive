package com.torontocodingcollective.sensors.encoder;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.playingwithfusion.CANVenom;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;

/**
 * TEncoder class used as the base for all TEncoders
 * <p>
 * The encoder interface is not consistent for PWM and CAN encoders, and this
 * interface is used to unify that interface
 * <p>
 * Known implementations: {@link TCanCtreEncoder}, {@link TDioQuadEncoder},
 * {@link TDioCounterEncoder}
 */
public class TEncoder {

	boolean isInverted = false;
	int     offset     = 0;

	private enum EncoderType { COUNTER, QUAD_ENCODER, TALON_SRX, SPARK_MAX, VENOM };

	private final EncoderType encoderType;

	private TalonSRX         talonSRXMotorController = null;
	private RelativeEncoder  sparkMaxEncoder         = null;
	private CANVenom         venomMotorController    = null;
	private Counter          counter                 = null;
	private Encoder          quadEncoder             = null;


	private double prevEncoderPosition = 0;
	private int    encoderCountsPerRevolution = 1;

	/**
	 * Encoder constructor. Construct a Encoder given a TalonSRX device.
	 * The encoder must be a quadrature encoder plugged into the TalonSRX.
	 * <p>
	 * The encoder will be reset to zero when constructed
	 * @param talonSRX where the quadrature encoder is attached
	 * @param isInverted {@code true} if inverted, {@code false} otherwise
	 */
	public TEncoder(TalonSRX talonSRXMotorController, boolean isInverted) {

		this.encoderType             = EncoderType.TALON_SRX;
		this.isInverted              = isInverted;
		this.talonSRXMotorController = talonSRXMotorController;

		this.talonSRXMotorController.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,  0);
		this.talonSRXMotorController.setSelectedSensorPosition(0, 0, 0);
	}

	public TEncoder(CANSparkMax canSparkMax, boolean isInverted) {

		this.encoderType = EncoderType.SPARK_MAX;
		this.isInverted = isInverted;
		this.encoderCountsPerRevolution = 64;

		this.sparkMaxEncoder = canSparkMax.getEncoder();
		prevEncoderPosition = 0;
	}

	public TEncoder(CANVenom venomMotorController, boolean isInverted) {

		this.encoderType = EncoderType.VENOM;
		this.isInverted = isInverted;
		this.encoderCountsPerRevolution = 256;

		this.venomMotorController = venomMotorController;
	}

	/**
	 * Encoder constructor. Construct a Encoder on the given DIO channel.
	 * @param dioChannel The DIO channel. 0-9 are on-board, 10-25 are on the MXP port
	 * @param isInverted {@code true} if inverted, {@code false} otherwise
	 */
	public TEncoder(int dioChannel, boolean isInverted) {

		this.encoderType = EncoderType.COUNTER;
		this.isInverted = isInverted;
		this.counter = new Counter(dioChannel);

		// Distance per pulse is set to 1.0 to get the raw count and rate in counts/sec
		counter.setDistancePerPulse(1.0);
	}

	/**
	 * Encoder constructor. Construct a Quadrature Encoder on the given DIO channels.
	 * @param dioChannelA The DIO channel. 0-9 are on-board, 10-25 are on the MXP port
	 * @param dioChannelB The DIO channel. 0-9 are on-board, 10-25 are on the MXP port
	 * @param isInverted {@code true} if inverted, {@code false} otherwise
	 */
	public TEncoder(int dioChannelA, int dioChannelB, boolean isInverted) {

		this.encoderType = EncoderType.QUAD_ENCODER;
		this.isInverted  = isInverted;
		this.quadEncoder     = new Encoder(dioChannelA, dioChannelB);

		// Distance per pulse is set to 1.0 to get the raw count and rate in counts/sec
		quadEncoder.setDistancePerPulse(1.0);
	}

	/**
	 * Get the distance of this encoder
	 *
	 * @return distance in encoder counts
	 */
	public int get() {

		switch (encoderType) {

		case COUNTER:
			return get(counter.get());

		case QUAD_ENCODER:
			return get(quadEncoder.get());

		case TALON_SRX:
			return get((int) talonSRXMotorController.getSelectedSensorPosition(0));

		case SPARK_MAX:

			// Spark Max returns the position in revolutions
			// Multiply by encoder counts per revolution to get
			// an equivalent encoder counts.
			double encoderPos = sparkMaxEncoder.getPosition();

			// Filter out glitches in the encoder
			// reading where it can sometimes return zero.
			if (encoderPos == 0) {
				encoderPos = prevEncoderPosition;
			}
			prevEncoderPosition = encoderPos;

			// Convert the raw counts from a double to
			// an encoder count.
			return get((int) (encoderPos * encoderCountsPerRevolution));

		case VENOM:
			return get((int) (venomMotorController.getPosition() * encoderCountsPerRevolution));
		}

		return 0;
	}

	/**
	 * Invert the raw distance if required
	 *
	 * @param rawDistance
	 * @return int inverted distance
	 */
	private int get(int rawDistance) {

		if (isInverted) {
			rawDistance = -rawDistance;
		}

		return rawDistance + offset;
	}

	/**
	 * Get the rate (speed) of this encoder
	 *
	 * @return speed in encoder counts/second
	 */
	public double getRate() {

		switch (encoderType) {

		case COUNTER:
			return getRate(counter.getRate());

		case QUAD_ENCODER:
			return getRate(quadEncoder.getRate());

		case TALON_SRX:
			return getRate(talonSRXMotorController.getSelectedSensorVelocity(0));

		case SPARK_MAX:
			return getRate(sparkMaxEncoder.getVelocity() * encoderCountsPerRevolution);

		case VENOM:
			return getRate(venomMotorController.getSpeed() * encoderCountsPerRevolution);


		}

		return 0;
	}

	/**
	 * Invert the raw rate if required
	 *
	 * @param rawRate
	 * @return double raw rate inverted if required
	 */
	private double getRate(double rawRate) {

		if (isInverted) {
			return -rawRate;
		}

		return rawRate;
	}

	/**
	 * Returns whether the current speed controller is
	 * inverted
	 * @return {@code true} if inverted, {@code false} otherwise
	 */
	public boolean isInverted() {
		return this.isInverted;
	}

	/**
	 * Reset the encoder counts for this encoder
	 */
	public void reset() {
		// set the offset for this encoder in order to
		// get the distance to zero
		// clear the previous offset
		offset = 0;

		// set the offset to the current encoder counts
		// in order to zero the output.
		offset = -get();
	}

	/**
	 * Set the encoder counts to a known value
	 *
	 * @param encoderCount
	 *            to set the encoder to
	 */
	public void set(int encoderCount) {
		offset = 0;
		offset = -get() + encoderCount;

	}

	/**
	 * Set the encoder inversion
	 * <p>
	 * A call to setInverted also resets the encoder if the inversion changes
	 *
	 * @param isInverted
	 *            {@code true} if the output should be inverted {@code false}
	 *            otherwise
	 */
	public void setInverted(boolean isInverted) {

		if (encoderType == EncoderType.COUNTER) {
			System.out.println("Inversion is not supported for counter encoders");
			this.isInverted = false;
			return;
		}

		// If the inversion changes reset the encoder
		if (this.isInverted != isInverted) {
			this.isInverted = isInverted;
			reset();
		}
	}

}
