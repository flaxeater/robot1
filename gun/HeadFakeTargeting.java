package cmc.gun;
import java.awt.Point;

import robocode.*;
import robocode.util.Utils;

//robot
import cmc.shapes.Square;
import cmc.RadionAtascar;

public class HeadFakeTargeting extends Gun {
  ScannedRobotEvent e = null;
  public HeadFakeTargeting(RadionAtascar parent) {
    super(parent);
  }
  public void handleTargeting(ScannedRobotEvent e) {
    this.e = e;
    setMyCoords();
    setHeadOnBearing(e);
    handleTargeting();
  }
  public void handleTargeting() {
    final double FIREPOWER = 2;
    final double ROBOT_WIDTH = 16,ROBOT_HEIGHT = 16;
    // Variables prefixed with e- refer to enemy, b- refer to bullet and r- refer to robot
    final double eAbsBearing = p.getHeadingRadians() + e.getBearingRadians()+Math.PI;
    final double rX = p.getX(), rY = p.getY(),
        bV = Rules.getBulletSpeed(FIREPOWER);
    final double eX = rX + e.getDistance()*Math.sin(eAbsBearing),
        eY = rY + e.getDistance()*Math.cos(eAbsBearing),
//        eV = e.getVelocity(),
        eV = averageVelocity(),
        eHd = e.getHeadingRadians()+Math.PI;
    // These constants make calculating the quadratic coefficients below easier
    final double A = (eX - rX)/bV;
    final double B = eV/bV*Math.sin(eHd);
    final double C = (eY - rY)/bV;
    final double D = eV/bV*Math.cos(eHd);
    // Quadratic coefficients: a*(1/t)^2 + b*(1/t) + c = 0
    final double a = A*A + C*C;
    final double b = 2*(A*B + C*D);
    final double c = (B*B + D*D - 1);
    final double discrim = b*b - 4*a*c;
    if (discrim >= 0) {
        // Reciprocal of quadratic formula
        final double t1 = 2*a/(-b - Math.sqrt(discrim));
        final double t2 = 2*a/(-b + Math.sqrt(discrim));
        final double t = Math.min(t1, t2) >= 0 ? Math.min(t1, t2) : Math.max(t1, t2);
        // Assume enemy stops at walls
        final double endX = limit(
            eX + eV*t*Math.sin(eHd),
            ROBOT_WIDTH/2, p.getBattleFieldWidth() - ROBOT_WIDTH/2);
        final double endY = limit(
            eY + eV*t*Math.cos(eHd),
            ROBOT_HEIGHT/2, p.getBattleFieldHeight() - ROBOT_HEIGHT/2);
        p.setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(
            Math.atan2(endX - rX, endY - rY)
            - p.getGunHeadingRadians()));
        p.setFire(FIREPOWER);
        //set the sqaure to be drawn
        p.addShape(new Square((int)endX-5,(int)endY+5,10,(int)t));
        
    }
  }
  public double averageVelocity() {
    double time = calculateTime();
    double avg = 0;
    for(int i=0;i<time;i++) {
      avg+=Math.max(-8,e.getVelocity());
    }
    echo("Average Velocity = " + avg/time);
    return avg/time;
  }
  public double calculateTime() {
    final double FIREPOWER = 2;
    final double ROBOT_WIDTH = 16,ROBOT_HEIGHT = 16;
    // Variables prefixed with e- refer to enemy, b- refer to bullet and r- refer to robot
    final double eAbsBearing = p.getHeadingRadians() + e.getBearingRadians();
    final double rX = p.getX(), rY = p.getY(),
        bV = Rules.getBulletSpeed(FIREPOWER);
    final double eX = rX + e.getDistance()*Math.sin(eAbsBearing),
        eY = rY + e.getDistance()*Math.cos(eAbsBearing),
        eV = e.getVelocity(),
        eHd = e.getHeadingRadians();
    // These constants make calculating the quadratic coefficients below easier
    final double A = (eX - rX)/bV;
    final double B = eV/bV*Math.sin(eHd);
    final double C = (eY - rY)/bV;
    final double D = eV/bV*Math.cos(eHd);
    // Quadratic coefficients: a*(1/t)^2 + b*(1/t) + c = 0
    final double a = A*A + C*C;
    final double b = 2*(A*B + C*D);
    final double c = (B*B + D*D - 1);
    final double discrim = b*b - 4*a*c;
    if (discrim >= 0) {
        // Reciprocal of quadratic formula
        final double t1 = 2*a/(-b - Math.sqrt(discrim));
        final double t2 = 2*a/(-b + Math.sqrt(discrim));
        final double t = Math.min(t1, t2) >= 0 ? Math.min(t1, t2) : Math.max(t1, t2);
        // Assume enemy stops at walls
        final double endX = limit(
            eX + eV*t*Math.sin(eHd),
            ROBOT_WIDTH/2, p.getBattleFieldWidth() - ROBOT_WIDTH/2);
        final double endY = limit(
            eY + eV*t*Math.cos(eHd),
            ROBOT_HEIGHT/2, p.getBattleFieldHeight() - ROBOT_HEIGHT/2);
        Point end = new Point((int)endX,(int)endY);
        //distance / bullet Velocity
        return end.distance(myPosition)/bV;
    }
    return 0.0;
  }

 
  private double limit(double value, double min, double max) {
      return Math.min(max, Math.max(min, value));
  }
}
