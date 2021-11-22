package com.torontocodingcollective.sensors.encoder;

import com.playingwithfusion.CANVenom;

/**
 * TCanEncoder reads a quadrature encoder plugged into a TalonSRX
 * <p>
 * Extends {@link TEncoder}
 */
public class TCanVenomEncoder extends TEncoder {

	private CANVenom venomMotorController;

	/**
	 * Encoder constructor. Construct a Encoder given a TalonSRX device.
	 * The encoder must be a quadrature encoder plugged into the TalonSRX.
	 * <p>
	 * The encoder will be reset to zero when constructed
	 * @param venomMotorController where the quadrature encoder is attached
	 * @param isInverted {@code true} if inverted, {@code false} otherwise
	 */
	public TCanVenomEncoder(CANVenom venomMotorController, boolean isInverted) {
		super(isInverted);
		this.venomMotorController = venomMotorController;
	}

	@Override
	public int get() {
		// Convert the current position in motor revolutions to a count. The
		// specs for this motor are 256 counts per revolution.
		return super.get((int) (venomMotorController.getPosition()*256));
	}

	@Override
	public double getRate() {
		// rate in revolutions per second converted to encoder counts per second.
		return super.getRate(venomMotorController.getSpeed() * 256);
	}

}
