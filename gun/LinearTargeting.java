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
		double angleUnderXAxis = myX > enemyX ? 
					Math.acos(distanceEastWest/distanceToEnemy) : 
					Math.asin(distanceEastWest/distanceToEnemy);
		double angleOfEnemyVectorRelativeToHeadOnBearing = e.getHeadingRadians()+angleUnderXAxis;

		//double insideRightAngle = 
		double angleRatio = e.getVelocity() / Rules.getBulletSpeed(bulletPower);
		double maximumEscapeAngle = Math.sin(e.getHeadingRadians() - headOnBearing);
		double angleToImpact = Math.asin(angleRatio * maximumEscapeAngle);
		double linearBearing = headOnBearing + angleToImpact;
		p.setTurnGunRightRadians(Utils.normalRelativeAngle(linearBearing - p.getGunHeadingRadians()));
		p.setFire(bulletPower);
//p.out.println("angle under =" + angleUnderXAxis*180/Math.PI);
//p.out.println(" angle relative head on = " + angleOfEnemyVectorRelativeToHeadOnBearing * 180 / Math.PI);
p.out.println("Normal Head on Bearingis " + Utils.normalAbsoluteAngleDegrees(headOnBearing));
	}


}

