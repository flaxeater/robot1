package cmc.movement;
import java.awt.Point;
import robocode.*;
import robocode.util.Utils;

//import cmc.util.*;

//robot
import cmc.RadionAtascar;

import java.awt.Graphics2D;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
 
/**
 * Credits
 * MovementBot - A robot by Alisdair Owens
 */

public class Movement {
 
	public Vector<GravPoint> gravPoints;
	private RadionAtascar bot = null;
	private ScannedRobotEvent e = null;
 
	public Movement(RadionAtascar bot) {
		this.bot = bot;
		gravPoints = new Vector<GravPoint>();
	}
 
 
	static int turns = 0;
	public void handleMovementWithoutEnemy() {
		gravPoints.clear();
		addGravPoints(false);
		calculateForceAndMove();
	}
	public void handleMovement(ScannedRobotEvent e) {
		this.e = e;
		gravPoints.clear();
		addGravPoints(true);
		calculateForceAndMove();
	}
 
 
	private double midpointstrength = 0; // The strength of the gravity point in the middle of the field
	private int midpointcount = 0; // Number of turns since that strength was changed.
 
	private void addGravPoints(boolean withEnemy) {
		/** Cycle through all the enemies. **/
		if (withEnemy) {
			Point enemy = bot.getRadar().getEnemyPosition();
			//GO toward the enemy
			gravPoints.add(new GravPoint(enemy.x, enemy.y, +1000));
		}
 
		/** Cycle through all the enemy bullets. **/
 
		/**
		 * The next section adds a middle point with a random (positive or
		 * negative) strength. The strength changes every 5 turns, and goes
		 * between -1000 and 1000.
		 */
		midpointcount++;
		if (midpointcount > 5) {
			midpointcount = 0;
			midpointstrength = (Math.random() * 2000) - 1000;
		}
		gravPoints.add(new GravPoint(bot.getBattleFieldWidth() / 2, bot
				.getBattleFieldHeight() / 2, midpointstrength));
 
		/** Other examples of GravPoints * */
		// Corners
		int cornerPower = -1000;
		gravPoints.add(new GravPoint(bot.getBattleFieldWidth(), bot
				.getBattleFieldHeight(), cornerPower));
		gravPoints.add(new GravPoint(0, bot.getBattleFieldHeight(), cornerPower));
		gravPoints.add(new GravPoint(bot.getBattleFieldWidth(), 0, cornerPower));
		gravPoints.add(new GravPoint(0, 0, cornerPower));
 
		// GravPoint at random positions of the walls
		int randomSides = -0500;
		gravPoints.add(new GravPoint(Math.random() * bot.getBattleFieldWidth(),
				bot.getBattleFieldHeight(), randomSides));
		gravPoints.add(new GravPoint(bot.getBattleFieldWidth(), Math.random()
				* bot.getBattleFieldHeight(), randomSides));
		gravPoints.add(new GravPoint(0, Math.random()
				* bot.getBattleFieldHeight(), randomSides));
		gravPoints.add(new GravPoint(Math.random() * bot.getBattleFieldWidth(),
				0, randomSides));
 
	}
 
	private void calculateForceAndMove() {
		double xforce = 0;
		double yforce = 0;
		double force;
		double ang;
 
		Iterator<GravPoint> i = gravPoints.iterator();
		while (i.hasNext()) {
			GravPoint gravPoint = i.next();
			force = gravPoint.power
					/ Math.pow(getRange(bot.getX(), bot.getY(), gravPoint.x,
							gravPoint.y), 2);
			// Find the bearing from the point to us
			ang = normaliseBearing(Math.PI
					/ 2
					- Math.atan2(bot.getY() - gravPoint.y, bot.getX()
							- gravPoint.x));
			// Add the components of this force to the total force in their
			// respective directions
			xforce += Math.sin(ang) * force;
			yforce += Math.cos(ang) * force;
		}
 
		/**
		 * The following four lines add wall avoidance. They will only affect us
		 * if the bot is close to the walls due to the force from the walls
		 * decreasing at a power 3.
		 */
		xforce += 5000 / Math.pow(getRange(bot.getX(), bot.getY(), bot
				.getBattleFieldWidth(), bot.getY()), 3);
		xforce -= 5000 / Math.pow(getRange(bot.getX(), bot.getY(), 0, bot
				.getY()), 3);
		yforce += 5000 / Math.pow(getRange(bot.getX(), bot.getY(), bot.getX(),
				bot.getBattleFieldHeight()), 3);
		yforce -= 5000 / Math.pow(getRange(bot.getX(), bot.getY(), bot.getX(),
				0), 3);
 
		// Move in the direction of our resolved force.
		goTo(bot.getX() - xforce, bot.getY() - yforce);
	}
 
	/** Move towards an x and y coordinate **/
	private void goTo(double x, double y) {
		double dist = 20;
		double angle = Math.toDegrees(absbearing(bot.getX(), bot.getY(), x, y));
		double r = turnTo(angle);
		bot.setAhead(dist * r);
	}
 
	/**
	 * Turns the shortest angle possible to come to a heading, then returns the
	 * direction the the bot needs to move in.
	 */
	private int turnTo(double angle) {
		double ang;
		int dir;
		ang = normaliseBearing(bot.getHeading() - angle);
		if (ang > 90) {
			ang -= 180;
			dir = -1;
		} else if (ang < -90) {
			ang += 180;
			dir = -1;
		} else {
			dir = 1;
		}
		bot.setTurnLeft(ang);
		return dir;
	}
 
	// if a bearing is not within the -pi to pi range, alters it to provide the
	// shortest angle
	private double normaliseBearing(double ang) {
		if (ang > Math.PI)
			ang -= 2 * Math.PI;
		if (ang < -Math.PI)
			ang += 2 * Math.PI;
		return ang;
	}
 
	// returns the distance between two x,y coordinates
	private double getRange(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double h = Math.sqrt(xo * xo + yo * yo);
		return h;
	}
 
	// gets the absolute bearing between to x,y coordinates
	private double absbearing(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double h = getRange(x1, y1, x2, y2);
		if (xo > 0 && yo > 0) {
			return Math.asin(xo / h);
		}
		if (xo > 0 && yo < 0) {
			return Math.PI - Math.asin(xo / h);
		}
		if (xo < 0 && yo < 0) {
			return Math.PI + Math.asin(-xo / h);
		}
		if (xo < 0 && yo > 0) {
			return 2.0 * Math.PI - Math.asin(-xo / h);
		}
		return 0;
	}
 
	public void onPaint(Graphics2D g) {
		Enumeration<GravPoint> e = gravPoints.elements();
		while (e.hasMoreElements()) {
			GravPoint gp = e.nextElement();
			g.drawOval((int) gp.x - 5, (int) gp.y - 5, 20, 20);
		}
	}
 
	/** Holds the x, y, and strength info of a gravity point* */
	class GravPoint {
		public double x, y, power;
 
		public GravPoint(double pX, double pY, double pPower) {
			x = pX;
			y = pY;
			power = pPower;
		}
	}
}
