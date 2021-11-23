package frc.robot.commands.pneumatics;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;

import frc.robot.oi.OI;
import frc.robot.subsystems.PneumaticsSubsystem;

/**
 *
 */
public class DefaultPneumaticsCommand extends TSafeCommand {

	private static final String COMMAND_NAME =
			DefaultPneumaticsCommand.class.getSimpleName();

	private final PneumaticsSubsystem pneumaticsSubsystem;
	private final OI oi;

	public DefaultPneumaticsCommand(OI oi, PneumaticsSubsystem pneumaticsSubsystem) {

		super(TConst.NO_COMMAND_TIMEOUT);

		this.oi = oi;
		this.pneumaticsSubsystem = pneumaticsSubsystem;

		// Use requires() here to declare subsystem dependencies
		addRequirements(pneumaticsSubsystem);
	}

	@Override
	protected String getCommandName() { return COMMAND_NAME; }

	@Override
	protected String getParmDesc() {
		return super.getParmDesc();
	}

	// Called just before this Command runs the first time
	@Override
	public void initialize() {
		// Print the command parameters if this is the current
		// called command (it was not sub-classed)
		if (getCommandName().equals(COMMAND_NAME)) {
			logMessage(getParmDesc() + " starting");
		}
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {

		if (oi.getCompressorEnabled()) {
			pneumaticsSubsystem.enableCompressor();
		} else {
			pneumaticsSubsystem.disableCompressor();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		return false;
	}

}
