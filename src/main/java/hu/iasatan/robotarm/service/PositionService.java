package hu.iasatan.robotarm.service;

import hu.iasatan.robotarm.model.Angles;
import hu.iasatan.robotarm.model.Position;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private double baseHeight;
    private double handLength;
    private double upperArmLength;
    private double foreArmLength;

    public PositionService(double baseHeight, double handLength, double upperArmLength, double foreArmLength) {
        this.baseHeight = baseHeight;
        this.handLength = handLength;
        this.upperArmLength = upperArmLength;
        this.foreArmLength = foreArmLength;
    }

    public Angles convertToAngles(Position position) {
        Angles angles = new Angles();
        double R = calculateR(position);
        double D = Math.sqrt((handLength - baseHeight) * (handLength - baseHeight) + R * R);
        angles.setElbowAngle(180 - calculateElbowAngle(R, D));
        angles.setPanAngle(calculatePanAngle(angles.getElbowAngle(), D));
        angles.setTiltAngle(calculateTiltAngle(position, R));
        angles.setHandAngle(calculateHandAngle(angles.getPanAngle(), angles.getElbowAngle()));
        return angles;
    }

    private double calculateR(Position position) {
        double R;
        R = Math.sqrt(position.getX() * position.getX() + position.getY() * position.getY());
        R = Math.sqrt(position.getZ() * position.getZ() + R * R);
        return R;
    }

    private int calculateTiltAngle(Position position, double R) {
        double angle;
        angle = Math.asin(position.getY() / R);
        angle = Math.round(angle * 180 / Math.PI);
        return (int) angle;
    }

    private int calculateElbowAngle(double R, double D) {
        double angle;
        angle = Math.acos(((upperArmLength * upperArmLength) + (foreArmLength * foreArmLength) - (D * D)) / (2 * foreArmLength * upperArmLength));
        return (int) angle;
    }

    private int calculatePanAngle(int elbowAngle, double D) {
        double angle;
        double sTheta1, sTheta2;
        sTheta1 = Math.asin((handLength - baseHeight) / D);
        sTheta2 = Math.asin((upperArmLength / D) * Math.sin(elbowAngle));
        angle = sTheta1 + sTheta2;
        angle = Math.round((angle * 180 / Math.PI));
        return (int) angle;
    }

    private int calculateHandAngle(int panAngle, int elbowAngle) {
        double angle;
        angle = 270 - panAngle - elbowAngle;
        angle = angle - 90;
        if (angle > 180)
            angle = 0;
        else
            angle = 180 - angle;
        return (int) angle;
    }
}
