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

public class RandomTargeting extends Gun {
  ScannedRobotEvent e = null;
  double oldEnemyHeading;

  public RandomTargeting(RadionAtascar parent) {
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
    double randomGuessFactor = (Math.random() - .5) * 2;
    double bulletPower = 0.5;
    double maxEscapeAngle = Math.asin(8.0/(20 - (3 * bulletPower)));
    double firingAngle = randomGuessFactor * maxEscapeAngle;
    double absBearingToEnemy = e.getBearingRadians() + p.getHeadingRadians();

    echo("bullet power = "+ bulletPower);
    p.setTurnGunRightRadians(Utils.normalRelativeAngle(
        absBearingToEnemy + firingAngle - p.getGunHeadingRadians()));
    p.fire(bulletPower);
  }
}
