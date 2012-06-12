package cmc.gun;
import robocode.*;
import robocode.util.Utils;

public class LinearTargeting extends Gun {
	public LinearTargeting(AdvancedRobot parent) {
		super(parent);
	}
	public void handleTargeting(ScannedRobotEvent e) {
		//setup basic data points, my (x,y) enemy(x,y) distance of enmey
		super.handleTargeting(e);
		double distanceEastWest = Math.abs(myX - enemyX);
		double enemyHeading = e.getHeadingRadians();
		echo ("Enemy Heading " + e.getHeading());
		double angleRatio = e.getVelocity() / Rules.getBulletSpeed(bulletPower);
		double maximumEscapeAngle = Math.sin(e.getHeadingRadians() - headOnBearing);
		echo("Maximum Escape Angle = " +(maximumEscapeAngle*180/Math.PI));
		double angleToImpact = Math.asin(angleRatio * maximumEscapeAngle);
		double linearBearing = headOnBearing + angleToImpact;
		p.setTurnGunRightRadians(Utils.normalRelativeAngle(linearBearing - p.getGunHeadingRadians()));
		p.setFire(bulletPower);
		getAngleBetweenMeAndEnemyHeading(e);
//		echo("angle under =" + angleUnderXAxis*180/Math.PI);
//		echo(" angle relative head on = " + angleOfEnemyVectorRelativeToHeadOnBearing * 180 / Math.PI);
		echo("Normal Head on Bearingis " + Utils.normalAbsoluteAngleDegrees(headOnBearing));
	}
	private double getAngleBetweenMeAndEnemyHeading(ScannedRobotEvent e) {
		double normalHeadOnBearing = Utils.normalAbsoluteAngle(headOnBearing);
		double height = Math.abs(myX - enemyY);
		double enemyAngleOfImpact = Math.PI/2 - Math.acos(height/e.getDistance());
//		double angle = normalHeadOnBearing 
		echo("enemyAngleOfImpact = " + enemyAngleOfImpact*180/Math.PI);
		echo( "OR " + (180-Math.asin(height/e.getDistance())*180/Math.PI));
		return enemyAngleOfImpact;
	}


}

