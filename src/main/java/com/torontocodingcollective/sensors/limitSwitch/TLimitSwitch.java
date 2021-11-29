package com.torontocodingcollective.sensors.limitSwitch;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * TLimitSwitch implements a limit switch with the supplied default state
 */
public class TLimitSwitch {

	private final boolean     defaultState;

	public final DigitalInput limitSwitch;

	/**
	 * Normally Open limit switch
	 * Construct a normally open limit switch
	 * <p>
	 * Since the DIO port has a pull up resistor, the value
	 * of the limit switch will be true.
	 *
	 * @param port dio port
	 */
	public TLimitSwitch(int port) {
		this(port, true);
	}

	/**
	 * Limit Switch
	 * <p>
	 * Construct a limit switch and specify the default state.
	 * <p>
	 *
	 * @param port dio port
	 */
	public TLimitSwitch(int port, boolean defaultState) {

		limitSwitch = new DigitalInput(port);
		this.defaultState = defaultState;

	}

	public boolean atLimit() {
		return limitSwitch.get() != defaultState;
	}

}
