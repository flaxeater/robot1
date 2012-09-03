package cmc.gun;
import java.awt.Point;
import robocode.*;
import robocode.util.Utils;

//robot
import cmc.RadionAtascar;
public abstract class Gun {
  protected RadionAtascar p;
  protected double bulletPower = 3.0;
  protected double enemyX;
  protected double enemyY;
//  protected double myX;
//  protected double myY;
  protected Point myPosition;
  protected double headOnBearing;
  protected double degreesToRadiansRatio = 360/(2*Math.PI);
  protected double radiansToDegreesRatio = (2*Math.PI)/360;

  public Gun(RadionAtascar parent) {
    p = parent;
  }
  //Getters and Setters
  public void setBulletPower(double p) {
    bulletPower = p;
  }

  //abstracts
  public void handleTargeting(ScannedRobotEvent e) {
//    setEnemyCoords(e);
    setMyCoords();
    setHeadOnBearing(e);
  }
  public abstract void handleTargetingAndFire(ScannedRobotEvent e);


  //concretions
  public void setMyCoords() {
    myPosition = new Point((int)p.getX(), (int)p.getX());
    echo("My position " + myPosition);
  }
  /**
    Figure out the angle towards the enemy
  */
  public void setHeadOnBearing(ScannedRobotEvent e) {
    headOnBearing = p.getHeadingRadians() + e.getBearingRadians();
  }
  public double getHeadOnBearing() {
    return p.getRadar().getHeadonBearing();
  }
  protected void echo(String m) {
    p.out.println(m);
  }
  public String toString() {
    return "Abstract Gun";
  }
}
