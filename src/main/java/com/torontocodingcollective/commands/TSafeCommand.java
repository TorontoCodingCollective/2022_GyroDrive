package com.torontocodingcollective.commands;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.TUtil;
import com.torontocodingcollective.oi.TOi;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * TSafeCommand
 * <p>
 * This command is used as a base command for all other commands and supports
 * the features: <ls>
 * <li>end the command after a given timeout
 * <li>end the command when cancelled by the user </ls>
 */
public abstract class TSafeCommand extends CommandBase {

	private final Trigger cancelTrigger;
	private final double timeout;

	private Command waitCommand;
	private long startTime = 0;

	/**
	 * TSafeCommand
	 * <p>
	 * Construct a safe command with the default timeout
	 * {@link TConst#DEFAULT_COMMAND_TIMEOUT }
	 *
	 * @param oi
	 *            the TOi object that defines the cancel operation
	 *            {@link TOi#getCancelCommand()}
	 */
	public TSafeCommand(Trigger cancelTrigger) {
		this(TConst.DEFAULT_COMMAND_TIMEOUT, cancelTrigger);
	}

	/**
	 * TSafeCommand
	 * <p>
	 * Construct a safe command with unlimited timeout
	 *
	 * @param timeout
	 *            the time after which this command will end automatically a value
	 *            of {@link TConst#NO_COMMAND_TIMEOUT} will be used as an infinite
	 *            timeout.
	 * @param oi
	 *            the TOi object that defines the cancel operation
	 *            {@link TOi#getCancelCommand()}
	 */
	public TSafeCommand(double timeout, Trigger cancelTrigger) {
		this.cancelTrigger = cancelTrigger;
		this.timeout       = timeout;
	}

	/**
	 * Get the command name associated with this command
	 * @return
	 */
	protected abstract String getCommandName();

	protected String getParmDesc() {
		if (timeout >= 0) {
			return "Timeout" + timeout;
		}
		else {
			return "No timeout";
		}
	}

	/**
	 * Log a message generated by any command
	 * <p>
	 * The message will be marked with the current period
	 * and the estimated time remaining in the period
	 * @param message to log
	 */
	protected void logMessage(String message) {

		// Mark the message with the time and command name
		StringBuilder sb = new StringBuilder();
		if (DriverStation.isAutonomous()) {
			sb.append("Auto: ");
		}
		else {
			sb.append("Teleop: ");
		}

		// Round the match time to one decimal
		double matchTime = TUtil.round(DriverStation.getMatchTime(), 2);
		sb.append(matchTime).append(' ');

		sb.append(getCommandName()).append(" : ");

		sb.append(message);

		System.out.println(sb.toString());
	}

	@Override
	public void initialize() {

		waitCommand = new WaitCommand(timeout);
		startTime = System.currentTimeMillis();
	}

	@Override
	public boolean isFinished() {

		if (isCancelled()) {
			logMessage("command cancelled by user after "
					+ TUtil.round(
							(double) (System.currentTimeMillis() - startTime) / 1000, 2) + "s");
			return true;
		}

		if (!waitCommand.isScheduled()) {
			logMessage("command timed out after "
					+ TUtil.round(
							(double) (System.currentTimeMillis() - startTime) / 1000, 2) + "s");
			return true;
		}

		return false;
	}

	@Override
	public void end(boolean interrupted) {
		if (interrupted) {
			logMessage("interrupted");
		}
	}

	/**
	 * Is Command Cancelled
	 * <p>
	 * Returns {@code true} if the user cancels the command using operator controls
	 *
	 * @return {@code true} if the command is to be cancelled, {@code false}
	 *         otherwise.
	 */
	public boolean isCancelled() {
		if (cancelTrigger.get()) {
			return true;
		}
		return false;
	}
}
