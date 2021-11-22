package com.torontocodingcollective.sensors.gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI.Port;

public class TNavXGyro extends TGyro {

    private final AHRS navXGyro;

    public TNavXGyro() {
        this(false);
    }

    public TNavXGyro(boolean inverted) {
        super(inverted);
        this.navXGyro = new AHRS(Port.kMXP);
    }

    @Override
    public void calibrate() {
        super.setGyroAngle(0);
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public double getAngle() {
        return super.getAngle(navXGyro.getAngle());
    }

    @Override
    public double getPitch() {
        // Not sure why pitch and roll are reversed on the NavX.
        // FIXME: Test the pitch every year.
        return navXGyro.getRoll();
    }

    @Override
    public double getRate() {
        return super.getRate(navXGyro.getRate());
    }

    @Override
    public boolean supportsPitch() {
        return true;
    }

}
