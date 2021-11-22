package com.torontocodingcollective.commands;

import com.torontocodingcollective.oi.TOi;

/**
 *
 */
public class TDelayCommand extends TSafeCommand {

    private static final String COMMAND_NAME =
            TDelayCommand.class.getSimpleName();

    public TDelayCommand(double delayTime, TOi oi) {
        super(delayTime, oi);
    }

    @Override
    protected String getCommandName() { return COMMAND_NAME; }

    @Override
    protected String getParmDesc() {
        return super.getParmDesc();
    }

    @Override
    public void initialize() {
        // Only print the command start message
        // if this command was not subclassed
        if (getCommandName().equals(COMMAND_NAME)) {
            logMessage(getParmDesc() + " starting");
        }

        super.initialize();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {

        if (super.isFinished()) {
            logMessage("Delay finished");
            return true;
        }
        return false;
    }

}
