package frc.robot.commands;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.drive.TDriveTimeCommand;
import com.torontocodingcollective.commands.gyroDrive.TDriveOnHeadingDistanceCommand;
import com.torontocodingcollective.commands.gyroDrive.TRotateToHeadingCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.oi.AutoSelector;
import frc.robot.subsystems.DriveSubsystem;

/**
 * AutonomousCommand
 * <p>
 * This class extends the {@link SequentialCommandGroup} class which allows for a string of
 * commands to be chained together to create complex auto patterns.
 */
public class AutonomousCommand extends SequentialCommandGroup {

	public static final char LEFT   = 'L';
	public static final char RIGHT  = 'R';
	public static final char CENTER = 'C';

	/**
	 * Example Autonomous Selector and Command
	 */
	public AutonomousCommand(DriveSubsystem driveSubsystem) {

		// getting info
		String robotStartPosition = AutoSelector.getRobotStartPosition();
		String pattern            = AutoSelector.getPattern();

		// Print out the user selection and Game config for debug later
		System.out.println("Auto Command Configuration");
		System.out.println("--------------------------");
		System.out.println("Robot Position : " + robotStartPosition);
		System.out.println("Pattern        : " + pattern);

		/* ***********************************************************
		 *  Drive Straight using GyroPID control
		 *  ***********************************************************/
		if (pattern.equals(AutoSelector.PATTERN_STRAIGHT_WITH_PID)) {
			// Go forward 2 ft
			addCommands(
					new TDriveOnHeadingDistanceCommand(250, 0, .95, 15, TConst.BRAKE_WHEN_FINISHED,
							driveSubsystem));
		}

		/* ***********************************************************
		 *  Drive Straight with with no GyroPID control
		 *  ***********************************************************/
		if (pattern.equals(AutoSelector.PATTERN_STRAIGHT_NO_PID)) {
			// Go forward 2 ft
			addCommands(
					new TDriveTimeCommand(.95, 6, TConst.BRAKE_WHEN_FINISHED,
							driveSubsystem));
		}


		/* ***********************************************************
		 *  Drive forward 2ft and then drive a 3ft box pattern
		 *  ***********************************************************/
		if (pattern.equals(AutoSelector.PATTERN_BOX)) {

			addCommands(
					// Go forward 2ft
					// 24 in, 0 deg, .5 speed, 5 sec, Brake
					new TDriveOnHeadingDistanceCommand(24, 0, .5, 5, TConst.COAST_WHEN_FINISHED,
							driveSubsystem),


					// Make a 4 sided box movement

					new TDriveOnHeadingDistanceCommand(36, 0, .5, 5, TConst.BRAKE_WHEN_FINISHED,
							driveSubsystem),

					new TRotateToHeadingCommand(90,
							driveSubsystem),

					new TDriveOnHeadingDistanceCommand(36, 90, .5, 5, TConst.BRAKE_WHEN_FINISHED,
							driveSubsystem),

					new TRotateToHeadingCommand(180,
							driveSubsystem),

					new TDriveOnHeadingDistanceCommand(36, 180, .5, 5, TConst.BRAKE_WHEN_FINISHED,
							driveSubsystem),

					new TRotateToHeadingCommand(270,
							driveSubsystem),

					new TDriveOnHeadingDistanceCommand(36, 270, .5, 5, TConst.BRAKE_WHEN_FINISHED,
							driveSubsystem),

					new TRotateToHeadingCommand(0,
							driveSubsystem)
					);
		}
	}
}
