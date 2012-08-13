package cmc.gun;
//std libs
import java.awt.Graphics2D;
import java.awt.geom.*;

//robocode
import robocode.*;
import robocode.util.Utils;

//robot
import cmc.shapes.Square;
import cmc.RadionAtascar;

public class CircularTargeting extends Gun {
  ScannedRobotEvent e = null;
  double oldEnemyHeading;

  public CircularTargeting(RadionAtascar parent) {
		super(parent);
    oldEnemyHeading = 0;
  }

  public void handleTargeting(ScannedRobotEvent e) {
    this.e = e;
    setEnemyCoords(e);
    setMyCoords();
    setHeadOnBearing(e);
    handleTargeting();
  }
  public void handleTargeting() {
    double bulletPower = Math.min(3.0,p.getEnergy());
//    double myX = p.getX();
//    double myY = p.getY();
    double absoluteBearing = p.getHeadingRadians() + e.getBearingRadians();
//    double enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
//    double enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
    double enemyHeading = e.getHeadingRadians();
    double enemyHeadingChange = enemyHeading - oldEnemyHeading;
    double enemyVelocity = e.getVelocity();
    oldEnemyHeading = enemyHeading;

    double deltaTime = 0;
    double battleFieldHeight = p.getBattleFieldHeight(), 
    battleFieldWidth = p.getBattleFieldWidth();
    double predictedX = enemyX, predictedY = enemyY;
    while((++deltaTime) * (20.0 - 3.0 * bulletPower) < 
           Point2D.Double.distance(myX, myY, predictedX, predictedY)) {
      predictedX += Math.sin(enemyHeading) * enemyVelocity;
      predictedY += Math.cos(enemyHeading) * enemyVelocity;
      enemyHeading += enemyHeadingChange;
      if( predictedX < 18.0 || predictedY < 18.0 || predictedX > battleFieldWidth - 18.0
            || predictedY > battleFieldHeight - 18.0) {
        predictedX = Math.min(Math.max(18.0, predictedX), 
        battleFieldWidth - 18.0); 
        predictedY = Math.min(Math.max(18.0, predictedY), 
        battleFieldHeight - 18.0);
        break;
      }
    }
    double theta = Utils.normalAbsoluteAngle(Math.atan2(
    predictedX - p.getX(), predictedY - p.getY()));

    p.setTurnRadarRightRadians(Utils.normalRelativeAngle(
        absoluteBearing - p.getRadarHeadingRadians()));
    p.setTurnGunRightRadians(Utils.normalRelativeAngle(
        theta - p.getGunHeadingRadians()));
    p.fire(3);
  }
}
