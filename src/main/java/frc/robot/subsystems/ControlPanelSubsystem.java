package frc.robot.subsystems;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

/**
 *
 */
public class ControlPanelSubsystem extends TSubsystem {


    /**
     * A Rev Color Sensor V3 object is constructed with an I2C port as a
     * parameter. The device will be automatically initialized with default
     * parameters.
     */
    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    private final ColorMatch colorMatcher = new ColorMatch();

    public static final Color BLUE_TARGET = ColorMatch.makeColor(0.143, 0.427, 0.429);
    public static final Color GREEN_TARGET = ColorMatch.makeColor(0.197, 0.561, 0.240);
    public static final Color RED_TARGET = ColorMatch.makeColor(0.561, 0.232, 0.114);
    public static final Color YELLOW_TARGET = ColorMatch.makeColor(0.361, 0.524, 0.113);
    public static final Color UNKNOWN_TARGET = Color.kBlack;

    public ControlPanelSubsystem() {
        colorMatcher.addColorMatch(BLUE_TARGET);
        colorMatcher.addColorMatch(GREEN_TARGET);
        colorMatcher.addColorMatch(RED_TARGET);
        colorMatcher.addColorMatch(YELLOW_TARGET);
    }

    @Override
    public void init() {
        // Place initialization code here
    }

    /** Get the color sensor colour */
    public Color getColorSensorColor() {

        ColorMatchResult match = colorMatcher.matchClosestColor(colorSensor.getColor());

        if (match.color.equals(BLUE_TARGET)) {
            return BLUE_TARGET;
        }

        if (match.color.equals(GREEN_TARGET)) {
            return GREEN_TARGET;
        }

        if (match.color.equals(RED_TARGET)) {
            return RED_TARGET;
        }

        if (match.color.equals(YELLOW_TARGET)) {
            return YELLOW_TARGET;
        }

        return UNKNOWN_TARGET;
    }

    /** Get the text string for a target color */
    private String getColorName(Color color) {

        if (color.equals(BLUE_TARGET)) {
            return "Blue";
        }
        if (color.equals(GREEN_TARGET)) {
            return "Green";
        }
        if (color.equals(RED_TARGET)) {
            return "Red";
        }
        if (color.equals(YELLOW_TARGET)) {
            return "Yellow";
        }
        return "Unknown";
    }

    /** Get the required color from the */
    public Color getSpecifiedTargetColor() {

        String gameData;
        gameData = DriverStation.getGameSpecificMessage();

        if(gameData.length() > 0)
        {
            switch (gameData.charAt(0))
            {
            case 'B' :
                return BLUE_TARGET;
            case 'G' :
                return GREEN_TARGET;
            case 'R' :
                return RED_TARGET;
            case 'Y' :
                return YELLOW_TARGET;
            default :
                return UNKNOWN_TARGET;
            }
        } else {
            return UNKNOWN_TARGET;
        }
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void periodic() {

        SmartDashboard.putString("Color Sensor Color", getColorName(getColorSensorColor()));
        SmartDashboard.putString("Specified Target Color", getColorName(getSpecifiedTargetColor()));
        SmartDashboard.putString("Color Sensor(R, G, B)", "" + colorSensor.getRed() + ", "
                + colorSensor.getGreen() + ", " + colorSensor.getBlue());
    }

}
