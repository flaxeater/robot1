package cmc;
//std lib
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
//robocode
import robocode.*;
import robocode.util.Utils;
//robot
import cmc.gun.*;
import cmc.shapes.*;
import cmc.radar.*;
import cmc.movement.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * RadionAtascar - a robot by (your name here)
 */
public class RadionAtascar extends AdvancedRobot
{
  //CONSTANTS
  public static final int DEBUG=1;
  public static final int WARN=2;
  public static final int ERROR=3;
  public static final int VERBOSE=4;

  private Gun targeter =  new VirtualGunTargeting(this);
  private Radar radar = new Radar(this);
  public ArrayList<Shape> shapes = new ArrayList<Shape>();
  private Movement mover = new Movement(this);
  /**
   * run: RadionAtascar's default behavior
   */
  public void run() {
    mover.handleMovementWithoutEnemy();
    setColors(Color.red,Color.blue,Color.YELLOW); // body,gun,radar
    turnRadarRightRadians(Double.POSITIVE_INFINITY);
    // Robot main loop
    while(true) {
      // Replace the next 4 lines with any behavior you would like
      if ( getRadarTurnRemaining() == 0.0 ) {
        setTurnRadarRightRadians( Double.POSITIVE_INFINITY );
      }
      execute();
    }
  }
  //Getters and Setters
  //Helper Functions
  private void manageScanLock(ScannedRobotEvent e) {
    out.println("SCANNED!!");
    radar.handleScan(e);
  }
  private void manageFiringSolutions(ScannedRobotEvent e) {
    targeter.handleTargeting(e);
  }
  public void addShape(Shape s) {
    shapes.add(s);
  }
  //End Helper Functions
  //Event Handlers

  /**
   * onScannedRobot: What to do when you see another robot
   */
  public void onScannedRobot(ScannedRobotEvent e) {
    manageScanLock(e);
    manageFiringSolutions(e);
    mover.handleMovement(e);
  }

  /**
   * onHitByBullet: What to do when you're hit by a bullet
   */
  public void onHitByBullet(HitByBulletEvent e) {
    // Replace the next line with any behavior you would like
    setTurnLeft(90.0);
    setAhead(200);
  }
  
  /**
   * onHitWall: What to do when you hit a wall
   */
  public void onHitWall(HitWallEvent e) {
    // Replace the next line with any behavior you would like
    setBack(200);
  }  
  public void onPaint(Graphics2D g) {
    //draw them
    for (Shape s: shapes) {
      s.draw(g);
    }
    mover.onPaint(g);
  }
  //end Event Handlers
  //Getters and Setters
  public Radar getRadar() {
    return radar;
  }
  //UTILS
  protected void echo(String m) {
    echo(m,WARN);
  }
  protected void echo(String m, int level) {
    if (level<=WARN) {
      out.println(m);
    }
  }
}
