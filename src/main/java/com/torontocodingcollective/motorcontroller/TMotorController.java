package com.torontocodingcollective.motorcontroller;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.playingwithfusion.CANVenom;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.torontocodingcollective.sensors.encoder.TEncoder;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.motorcontrol.DMC60;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.PWMVenom;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.SD540;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;

/**
 * Common interface for all FIRST MotorControllers
 * <p>
 * All TMotorControllers implement the WPILib {@link MotorController} interface
 */
public class TMotorController extends MotorSafety implements MotorController, Sendable, AutoCloseable {

	/**
	 * Enum of all PWM speed controller types supported by the WPILib.
	 */
	public enum TMotorControllerType {
		/*
		 * CAN Bus Motor Controller Types
		 */
		/** SparkMax connected to a brushless motor */
		SPARK_MAX_CAN_BRUSHLESS,
		/** SparkMax connected to a brushed (non-NEO brushless) motor */
		SPARK_MAX_CAN_BRUSHED,
		/** Talon SRX connected to the CAN bus */
		TALON_SRX_CAN,
		/** Venom connected to the CAN bus */
		VENOM_CAN,
		/** Victor SPX connected to the CAN bus */
		VICTOR_SPX_CAN,
		/*
		 * PWM Motor Controller Types
		 */
		/** DMC60 see {@link DMC60} */
		DMC60,
		/** Jaguar see {@link Jaguar} */
		JAGUAR,
		/** SD540 see {@link SD540} */
		SD540,
		/** Spark see {@link Spark} */
		SPARK,
		/** Spark see {@link Spark} */
		SPARK_MAX_PWM,
		/** Talon see {@link Talon} */
		TALON,
		/** TalonFX see {@link PWMTalonFx} with PWM connection */
		TALON_FX_PWM,
		/** TalonSRX see {@link PWMTalonSRX} */
		TALON_SRX_PWM,
		/** Venom {@link PWMVenom} with PWM Connection */
		VENOM_PWM,
		/** Victor 888 or 884 see {@link Victor} */
		VICTOR,
		/** VictorSP see {@link VictorSP} */
		VICTOR_SP,
		/** VictorSPX see {@link PWMVictorSPX} with PWM connection */
		VICTOR_SPX_PWM
	}

	private class TMotorControllerEntry {

		/** The type of this motor */
		private final TMotorControllerType motorControllerType;
		/** The address for this motorController */
		private final int address;
		/** The motor inversion which can be changed using setInverted */
		private boolean              isInverted;
		/*
		 * All possible object instantiations for this Motor Controller
		 */
		/** WPILib PWM Motor Controllers */
		private final PWMMotorController pwmMotorController;
		/** CTRE Motor Controllers */
		private final com.ctre.phoenix.motorcontrol.can.BaseMotorController canCtreMotorController;
		/** Spark Max Motor Controllers */
		private final com.revrobotics.CANSparkMax                           canSparkMotorController;
		/** Venom Motor Controller */
		private final com.playingwithfusion.CANVenom                        canVenom;

		private TMotorControllerEntry(TMotorControllerType motorControllerType, int address, boolean isInverted) {

			this.motorControllerType = motorControllerType;
			this.address        = address;
			this.isInverted     = isInverted;

			/*
			 * Check for a vendor specific can connected device.
			 */
			switch (motorControllerType) {
			case SPARK_MAX_CAN_BRUSHED:
				canSparkMotorController = new CANSparkMax(address, MotorType.kBrushed);
				canCtreMotorController = null;
				canVenom = null;
				pwmMotorController = null;
				return;

			case SPARK_MAX_CAN_BRUSHLESS:
				canSparkMotorController = new CANSparkMax(address, MotorType.kBrushless);
				canCtreMotorController = null;
				canVenom = null;
				pwmMotorController = null;
				return;

			case VENOM_CAN:
				canVenom = new CANVenom(address);
				canCtreMotorController = null;
				canSparkMotorController = null;
				pwmMotorController = null;
				return;

			case TALON_SRX_CAN:
				canCtreMotorController = new TalonSRX(address);
				canSparkMotorController = null;
				canVenom = null;
				pwmMotorController = null;
				return;

			case VICTOR_SPX_CAN:
				canCtreMotorController = new VictorSPX(address);
				canSparkMotorController = null;
				canVenom = null;
				pwmMotorController = null;
				return;

			default:
				break;
			}

			PWMMotorController pwmMotorController = null;
			switch (motorControllerType) {
			case DMC60:	        	pwmMotorController = new DMC60(address);        break;
			case JAGUAR:     		pwmMotorController = new Jaguar(address);		break;
			case SD540:	        	pwmMotorController = new SD540(address);        break;
			case SPARK:         	pwmMotorController = new Spark(address);        break;
			case SPARK_MAX_PWM: 	pwmMotorController = new PWMSparkMax(address);  break;
			case TALON:         	pwmMotorController = new Talon(address);        break;
			case TALON_FX_PWM:  	pwmMotorController = new PWMTalonFX(address);   break;
			case TALON_SRX_PWM:  	pwmMotorController = new PWMTalonSRX(address);  break;
			case VENOM_PWM:      	pwmMotorController = new PWMVenom(address);     break;
			case VICTOR:      	    pwmMotorController = new Victor(address);       break;
			case VICTOR_SP:      	pwmMotorController = new VictorSP(address);     break;
			case VICTOR_SPX_PWM:    pwmMotorController = new PWMVictorSPX(address);	break;
			default:
				break;
			}

			if (pwmMotorController != null) {
				this.pwmMotorController = pwmMotorController;
				this.canCtreMotorController = null;
				this.canSparkMotorController = null;
				this.canVenom = null;
				return;
			}

			this.pwmMotorController = null;
			this.canCtreMotorController = null;
			this.canSparkMotorController = null;
			this.canVenom = null;
		}
	}

	private List<TMotorControllerEntry> motorControllerList = new ArrayList<TMotorControllerEntry>();

	/**
	 * Motor Controller
	 * <p>
	 * Supports any number of Motor Controllers of the same type driving in the
	 * same direction. The same output signal will be sent to all ports
	 * associated with this controller.
	 *
	 * @param controllerType
	 *            a valid {@link TMotorControllerType}
	 * @param address
	 *            the CAN address, or PWM port of the primary motor controller
	 * @param inverted
	 *            {@code true} if all the motors are inverted, {@code false} otherwise
	 * @param followerAddresses
	 *            the CAN address, or PWM port of the primary motor controller
	 */
	public TMotorController(TMotorControllerType controllerType, int address, boolean inverted,
			int... followerAddresses) {
		motorControllerList.add(new TMotorControllerEntry(controllerType, address, inverted));
		for (int followerAddress : followerAddresses) {
			motorControllerList.add(new TMotorControllerEntry(controllerType, followerAddress, inverted));
		}
	}

	/**
	 * Motor Controller
	 * <p>
	 * Supports any number of Motor Controllers of the same type driving in the
	 * same direction. The same output signal will be sent to all ports
	 * associated with this controller.
	 *
	 * @param controllerType
	 *            a valid {@link TMotorControllerType}
	 * @param address
	 *            the CAN address, or PWM port of the primary motor controller
	 * @param followerAddresses
	 *            the CAN address, or PWM port of the primary motor controller
	 */
	public TMotorController(TMotorControllerType controllerType, int address, int... followerAddresses) {
		this(controllerType, address, false, followerAddresses);
	}

	/**
	 * PWM Speed Controller
	 * <p>
	 * Supports two PWM speed controllers of different types driving the same drive
	 * train. The same output signal will be sent to the follower PWM ports.
	 *
	 * @param controllerType
	 *            a valid {@link MotorControllerType}
	 * @param address
	 *            the CAN address, or PWM port of the primary motor controller
	 * @param followerControllerType
	 *            a valid {@link TPWMControllerType}
	 * @param followerAddress
	 *            the CAN address, or PWM port of the follower controller
	 */
	public TMotorController(TMotorControllerType controllerType, int address,
			TMotorControllerType followerControllerType, int followerAddress) {
		this(controllerType, address, followerControllerType, followerAddress, false);
	}

	/**
	 * PWM Speed Controller
	 * <p>
	 * Supports two PWM speed controllers of different types driving the same drive
	 * train. The same output signal will be sent to the follower PWM ports.
	 *
	 * @param controllerType
	 *            a valid {@link MotorControllerType}
	 * @param address
	 *            the CAN address, or PWM port of the primary motor controller
	 * @param followerControllerType
	 *            a valid {@link TPWMControllerType}
	 * @param followerAddress
	 *            the CAN address, or PWM port of the follower controller
	 * @param inverted
	 *            {@code true} if the motors are inverted, {@code false} otherwise
	 */
	public TMotorController(TMotorControllerType controllerType, int address,
			TMotorControllerType followerControllerType, int followerAddress, boolean isInverted) {

		motorControllerList.add(
				new TMotorControllerEntry(controllerType, address, isInverted));
		motorControllerList.add(
				new TMotorControllerEntry(followerControllerType, followerAddress, isInverted));
	}

	@Override
	public double get() {

		if (motorControllerList.isEmpty()) {
			return 0;
		}

		double speed = 0;

		// Get the primary motor and return its speed setting
		TMotorControllerEntry motorControllerEntry = motorControllerList.get(0);

		if (motorControllerEntry.canCtreMotorController != null) {
			speed = motorControllerEntry.canCtreMotorController.getMotorOutputPercent();
		}

		if (motorControllerEntry.canSparkMotorController != null) {
			speed = motorControllerEntry.canSparkMotorController.get();
		}

		if (motorControllerEntry.canVenom != null) {
			speed = motorControllerEntry.canVenom.get();
		}

		if (motorControllerEntry.pwmMotorController != null) {
			speed = motorControllerEntry.pwmMotorController.get();
		}

		if (motorControllerEntry.isInverted) {
			speed = -speed;
		}

		return speed;
	}

	@Override
	public void set(double speed) {

		// For each motor in the group, set the motor speed
		for (TMotorControllerEntry motorControllerEntry: motorControllerList) {

			double entrySpeed = speed;

			if (motorControllerEntry.isInverted) {
				entrySpeed *= -1.0;
			}

			if (motorControllerEntry.canCtreMotorController != null) {
				motorControllerEntry.canCtreMotorController.set(ControlMode.PercentOutput, entrySpeed);
				continue;
			}

			if (motorControllerEntry.canSparkMotorController != null) {
				motorControllerEntry.canSparkMotorController.set(entrySpeed);
				continue;
			}

			if (motorControllerEntry.canVenom != null) {
				motorControllerEntry.canVenom.set(entrySpeed);
				continue;
			}

			if (motorControllerEntry.pwmMotorController != null) {
				motorControllerEntry.pwmMotorController.set(entrySpeed);
			}
		}
	}

	@Override
	public void disable() {
		stopMotor();
	}

	@Override
	public boolean getInverted() {

		if (motorControllerList.isEmpty()) {
			return false;
		}

		// Get the primary motor and return its inversion setting
		// NOTE: Not all motors in the group need to have the same inversion
		TMotorControllerEntry motorControllerEntry = motorControllerList.get(0);

		return motorControllerEntry.isInverted;
	}

	@Override
	public void setInverted(boolean isInverted) {

		// DANGER: in order to not burn out motors, all of the
		//         group must be inverted at once.
		if (this.motorControllerList.isEmpty()) {
			return;
		}

		// If the inversion is already correct, then there
		// is nothing to do.
		if (motorControllerList.get(0).isInverted == isInverted) {
			return;
		}

		// Otherwise, flip all controllers in the group
		for (TMotorControllerEntry motorControllerEntry: motorControllerList) {
			motorControllerEntry.isInverted = !motorControllerEntry.isInverted;
		}
	}

	@Override
	public void stopMotor() {
		set(0);
	}

	public List<Object> getNativeMotorControllers() {

		List<Object> motorControllers = new ArrayList<>();

		for (TMotorControllerEntry motorControllerEntry: motorControllerList) {

			if (motorControllerEntry.canCtreMotorController != null) {
				motorControllers.add(motorControllerEntry.canCtreMotorController);
				continue;
			}

			if (motorControllerEntry.canSparkMotorController != null) {
				motorControllers.add(motorControllerEntry.canSparkMotorController);
				continue;
			}

			if (motorControllerEntry.canVenom != null) {
				motorControllers.add(motorControllerEntry.canVenom);
				continue;
			}

			if (motorControllerEntry.pwmMotorController != null) {
				motorControllers.add(motorControllerEntry.pwmMotorController);
			}
		}

		return motorControllers;
	}

	/**
	 * Get the encoder attached to this TSpeedController
	 * <p>
	 * By default, the encoder will be set with the same inversion setting as the
	 * motor and is assumed to be a 2-channel quadrature encoder.
	 *
	 * @returns TEncoder attached to this device or {@code null} if this device does
	 *          not support an attached encoder
	 */
	public TEncoder getEncoder() {

		if (motorControllerList.isEmpty()) {
			return null;
		}

		// Get the encoder from the first entry in the list
		TMotorControllerEntry motorControllerEntry = motorControllerList.get(0);

		if (motorControllerEntry.canCtreMotorController != null) {
			if (motorControllerEntry.canCtreMotorController instanceof TalonSRX) {
				return new TEncoder((TalonSRX) motorControllerEntry.canCtreMotorController, getInverted());
			}
		}

		if (motorControllerEntry.canSparkMotorController != null) {
			return new TEncoder(motorControllerEntry.canSparkMotorController, getInverted());
		}

		if (motorControllerEntry.canVenom != null) {
			return new TEncoder(motorControllerEntry.canVenom, getInverted());
		}

		System.out.println("GetEncoder is not supported for " + motorControllerEntry.motorControllerType);

		return null;
	}

	@Override
	public void close() throws Exception {

		for (TMotorControllerEntry motorControllerEntry: motorControllerList) {

			// CTRE controllers cannot be closed.
			if (motorControllerEntry.canCtreMotorController != null) {
				continue;
			}

			if (motorControllerEntry.canSparkMotorController != null) {
				motorControllerEntry.canSparkMotorController.close();
				continue;
			}

			if (motorControllerEntry.canVenom != null) {
				motorControllerEntry.canVenom.close();
				continue;
			}

			if (motorControllerEntry.pwmMotorController != null) {
				motorControllerEntry.pwmMotorController.close();
			}
		}
	}

	@Override
	public void initSendable(SendableBuilder builder) {

		builder.setSmartDashboardType("Motor Controller");
		builder.setActuator(true);
		builder.setSafeState(this::disable);
		builder.addDoubleProperty("Value", this::get, this::set);
	}

	@Override
	public String getDescription() {

		// Return a description for this group showing all controllers in the group.
		if (motorControllerList.isEmpty()) {
			return "No controllers in this group";
		}

		StringBuilder desc = new StringBuilder();

		for (TMotorControllerEntry motorControllerEntry: motorControllerList) {

			if (desc.length() != 0) {
				desc.append('|');
			}

			desc.append(motorControllerEntry.motorControllerType);

			desc.append(':').append(motorControllerEntry.address);

			if (motorControllerEntry.isInverted) {
				desc.append("(I)");
			}
		}

		if (desc.length() == 0) {
			return "Unknown Motor Controller Type";
		}

		return desc.toString();
	}

}
