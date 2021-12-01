package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 *
 */
public class PneumaticsSubsystem extends SubsystemBase {

	Compressor compressor = null;

	public PneumaticsSubsystem() {

		// Make a new compressor based on the module
		compressor = new Compressor(Constants.PNEUMATICS_MODULE_TYPE);

		if (compressor != null) {
			compressor.setClosedLoopControl(true);
		}
	}

	public void disableCompressor() {
		if (compressor != null) {
			compressor.setClosedLoopControl(false);
		}
	}

	public void enableCompressor() {
		if (compressor != null) {
			compressor.setClosedLoopControl(true);
		}
	}

	// Periodically update the dashboard and any PIDs or sensors
	@Override
	public void periodic() {

		if (compressor != null) {
			SmartDashboard.putBoolean("Compressor", compressor.enabled());
			SmartDashboard.putBoolean("Compressor Enabled", compressor.getClosedLoopControl());
		} else {
			SmartDashboard.putBoolean("Compressor", false);
			SmartDashboard.putBoolean("Compressor Enabled", false);
		}
	}

}
