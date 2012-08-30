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
}
