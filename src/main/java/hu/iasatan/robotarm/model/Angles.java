package hu.iasatan.robotarm.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Angles {
    private int panAngle;
    private int tiltAngle;
    private int elbowAngle;
    private int handAngle;
}
