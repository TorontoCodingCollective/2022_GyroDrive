package frc.robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Power Subsystem
 * <p>
 * This subsystem contains only the Power Distribution Panel and is used to read
 * the current on a power port.
 */
public class PowerSubsystem extends TSubsystem {

	PowerDistribution pdp = new PowerDistribution();

	public double getMotorCurrent(int port) {
		return pdp.getCurrent(port);
	};

	@Override
	public void init() {
	}

	// Periodically update the dashboard and any PIDs or sensors
	@Override
	public void periodic() {
		SmartDashboard.putData("PDP", pdp);
	}

}
