package cmc.gun;
import robocode.*;
import robocode.util.Utils;
public abstract class Gun {
	protected AdvancedRobot p;
	protected double bulletPower = 3.0;
	protected double enemyX;
	protected double enemyY;
	protected double myX;
	protected double myY;
	protected double headOnBearing;
	protected double distanceToEnemy;
	protected double degreesToRadiansRatio = 360/(2*Math.PI);
	protected double radiansToDegreesRatio = (2*Math.PI)/360;

	public Gun(AdvancedRobot parent) {
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
		setDistanceToEnemy(e);
	}


	//concretions
	public void setEnemyCoords(ScannedRobotEvent e) {
		enemyX = p.getX()+ distanceToEnemy * Math.sin(headOnBearing);
		enemyY = p.getY()+ distanceToEnemy * Math.cos(headOnBearing);
	}
	public void setMyCoords() {
		myX = p.getX();
		myY = p.getY();
	}
	/**
		Figure out the angle towards the enemy
	*/
	public void setHeadOnBearing(ScannedRobotEvent e) {
p.out.println("MY Heading " + p.getHeading());
p.out.println("Enemy Bearing " + e.getBearing());
		headOnBearing = p.getHeadingRadians() + e.getBearingRadians();
//p.out.println("Head on Bearings Degrees " + Utils.normalAbsoluteAngle(headOnBearing)*radiansToDegreesRatio);
	}
	public void setDistanceToEnemy(ScannedRobotEvent e) {
		distanceToEnemy = e.getDistance();
//p.out.println("Distance To Enemy " + distanceToEnemy);
	}
}
