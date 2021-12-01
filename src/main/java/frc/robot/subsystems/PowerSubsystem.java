package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Power Subsystem
 * <p>
 * This subsystem contains only the Power Distribution Panel and is used to read
 * the current on a power port.
 */
public class PowerSubsystem extends SubsystemBase {

	PowerDistribution pdp = new PowerDistribution();

	public double getMotorCurrent(int port) {
		return pdp.getCurrent(port);
	};

	// Periodically update the dashboard and any PIDs or sensors
	@Override
	public void periodic() {
		SmartDashboard.putData("PDP", pdp);
	}

}
