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

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * RadionAtascar - a robot by (your name here)
 */
public class RadionAtascar extends AdvancedRobot
{
	private Gun targeter =  new QuadraticLinearTargeting(this);
  public ArrayList<Shape> shapes = new ArrayList<Shape>();
	/**
	 * run: RadionAtascar's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setColors(Color.red,Color.blue,Color.YELLOW); // body,gun,radar

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
//		fire(1);
		//absolute angle to enemy
		double angleToEnemy = getHeadingRadians() + e.getBearingRadians();
		//get the angle the radar needs to turn to
		double radarTurn = Utils.normalRelativeAngle(angleToEnemy - getRadarHeadingRadians());
		double extraTurn = Math.min(Math.atan(36.0/e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS);
		radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
		setTurnRadarRightRadians(radarTurn);
		setTurnGunRightRadians(radarTurn);
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
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
  public void onPaint(Graphics2D g) {
    //draw them
    for (Shape s: shapes) {
      s.draw(g);
    }
  }
	//end Event Handlers
}
