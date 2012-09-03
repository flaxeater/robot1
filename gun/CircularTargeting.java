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

  private void setupHandleTargeting(ScannedRobotEvent e) {
    this.e = e;
    super.handleTargeting(e);
  }
  public void handleTargeting(ScannedRobotEvent e) {
    setupHandleTargeting(e);
    handleTargeting(false);
  }
  public void handleTargetingAndFire(ScannedRobotEvent e) {
    setupHandleTargeting(e);
    handleTargeting(true);
  }
  private void handleTargeting(boolean fire) {
    double bulletPower = Math.min(3.0,p.getEnergy());
    double absoluteBearing = p.getHeadingRadians() + e.getBearingRadians();
    double enemyHeading = e.getHeadingRadians();
    double enemyHeadingChange = enemyHeading - oldEnemyHeading;
    double enemyVelocity = e.getVelocity();
    oldEnemyHeading = enemyHeading;
    double deltaTime = 0;
    double battleFieldHeight = p.getBattleFieldHeight(), 
    battleFieldWidth = p.getBattleFieldWidth();
    double predictedX = p.getRadar().getEnemyPosition().getX();
    double predictedY = p.getRadar().getEnemyPosition().getY();
    while((++deltaTime) * (20.0 - 3.0 * bulletPower) < 
           Point2D.Double.distance(
            (double)myPosition.getX(), 
            (double)myPosition.getY(), 
            predictedX, 
            predictedY)) {
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

    p.setTurnGunRightRadians(Utils.normalRelativeAngle( theta - p.getGunHeadingRadians()));
    p.setFire(3);
    p.addShape(new Square((int)predictedX-5,(int)predictedY+5,10,(int)deltaTime));
  }
  public String toString() {
    return "CircularTargeting";
  }
  public void echo(String m) {
    p.out.println(m);
  }
}
