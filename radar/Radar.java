package cmc.radar;
import java.awt.Point;
import robocode.*;
import robocode.util.Utils;

//import cmc.util.*;

//robot
import cmc.RadionAtascar;
public class Radar {
  protected RadionAtascar p;
  protected double headOnBearing;
  protected Point enemyPoint;

  public Radar(RadionAtascar parent) {
    p = parent;
  }

  //abstracts
  public void handleScan(ScannedRobotEvent e) {
    setEnemyCoords(e);
    setHeadonBearings(e);
    //absolute angle to enemy
    scan(e);
    checkForFiredEvent(e);
  }
  private void scan(ScannedRobotEvent e) {
    //get the angle the radar needs to turn to
    double radarTurn = Utils.normalRelativeAngle(headOnBearing - p.getRadarHeadingRadians());
    double extraTurn = Math.min(Math.atan(36.0/e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS);
    //adding a little bit more backwards turn when compensating backwards
    radarTurn += (radarTurn < 0 ? -extraTurn-6/Math.PI : extraTurn);
    p.setTurnRadarRightRadians(radarTurn);
  }

  //concretions
  public void setEnemyCoords(ScannedRobotEvent e) {
    double enemyX = p.getX()+ e.getDistance() * Math.sin(headOnBearing);
    double enemyY = p.getY()+ e.getDistance() * Math.cos(headOnBearing);
    enemyPoint = new Point((int)enemyX,(int)enemyY);

  }
  private void setHeadonBearings(ScannedRobotEvent e) {
    headOnBearing = p.getHeadingRadians() + e.getBearingRadians();
  }
  public double getHeadonBearing() {
    return headOnBearing;
  }
  public Point getEnemyPosition() {
    return enemyPoint;
  }

  //the energy level will not be 500 so it's over
  private static double enemyEnergy = -20;
  private static boolean enemyFired = false;
  private void checkForFiredEvent(ScannedRobotEvent e) {
    double energy = e.getEnergy();
    //initializtion check or something strange
    if (enemyEnergy < 0 || energy > enemyEnergy) {
        enemyEnergy = energy;
    }
    //bullet fired
    double diff = enemyEnergy - energy;
    if (enemyEnergy > energy &&  diff >= 0.1 && diff <= 3.0) {
        p.out.println("FIRED");
        enemyFired = true;
    }
    else {
        enemyFired = false;
    }
  }
  public boolean enemyFired() {
    return enemyFired;
  }
}
