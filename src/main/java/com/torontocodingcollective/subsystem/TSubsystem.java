package com.torontocodingcollective.subsystem;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * TSubsystem
 * <p>
 * Extends the CommandBased Subsystem to add init() and updatePeriodic
 */
public abstract class TSubsystem extends SubsystemBase {

    /**
     * Initialize the subsystem
     */
    public abstract void init();

}
