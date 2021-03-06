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

public class HeadOnTargeting extends Gun {
  ScannedRobotEvent e = null;
  double oldEnemyHeading;

  public HeadOnTargeting(RadionAtascar parent) {
    super(parent);
    oldEnemyHeading = 0;
  }

  public void handleTargetingAndFire(ScannedRobotEvent e) {
    this.e = e;
    setMyCoords();
    setHeadOnBearing(e);
    handleTargeting(true);
  }
  public void handleTargeting(ScannedRobotEvent e) {
    this.e = e;
    setMyCoords();
    setHeadOnBearing(e);
    handleTargeting(false);
  }
  public void handleTargeting(boolean fire) {
    double absoluteBearing = p.getHeadingRadians() + e.getBearingRadians();
    double turnRightRadians = robocode.util.Utils.normalRelativeAngle(absoluteBearing - p.getGunHeadingRadians());
    p.setTurnGunRightRadians(turnRightRadians);
    if (fire) p.setFire(bulletPower);
  }
  public String toString() {
    return "HeadOnTargeting";
  }
}
