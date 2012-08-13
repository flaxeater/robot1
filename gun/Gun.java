package cmc.gun;
import robocode.*;
import robocode.util.Utils;

//robot
import cmc.RadionAtascar;
public abstract class Gun {
  protected RadionAtascar p;
  protected double bulletPower = 3.0;
  protected double enemyX;
  protected double enemyY;
  protected double myX;
  protected double myY;
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
    setEnemyCoords(e);
    setMyCoords();
    setHeadOnBearing(e);
  }


  //concretions
  public void setEnemyCoords(ScannedRobotEvent e) {
    enemyX = p.getX()+ e.getDistance() * Math.sin(headOnBearing);
    enemyY = p.getY()+ e.getDistance() * Math.cos(headOnBearing);
    echo("My X = " + (int)p.getX() + " My Y = " + (int)p.getY() + " Enemy X,Y " + (int)enemyX + "," + (int)enemyY);
  }
  public void setMyCoords() {
    myX = p.getX();
    myY = p.getY();
  }
  /**
    Figure out the angle towards the enemy
  */
  public void setHeadOnBearing(ScannedRobotEvent e) {
//    echo("My Heading " + p.getHeading());
//    echo("Enemy Bearing " + e.getBearing());
    headOnBearing = p.getHeadingRadians() + e.getBearingRadians();
//    echo("Head On Bearing = " + Utils.normalAbsoluteAngleDegrees(p.getHeading() + e.getBearing()));
  }
  protected void echo(String m) {
    p.out.println(m);
  }
}
