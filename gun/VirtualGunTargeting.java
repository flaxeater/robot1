package cmc.gun;
//std libs
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

//robocode
import robocode.*;
import robocode.util.Utils;

//robot
import cmc.shapes.Square;
import cmc.RadionAtascar;
import cmc.gun.*;

public class VirtualGunTargeting extends Gun {
  ScannedRobotEvent e = null;
  private ArrayList<Gun> guns = new ArrayList<Gun>();
  private Random randomGenerator = new Random();
  private double pastGunHeat = 0;
  private Gun currentGun = null;

  public VirtualGunTargeting(RadionAtascar parent) {
    super(parent);
    //Add the guns
    guns.add(new RandomTargeting(p));
    guns.add(new CircularTargeting(p));
    guns.add(new QuadraticLinearTargeting(p));
    guns.add(new HeadOnTargeting(p));
  }

  public Gun getRandomGun() {
    int index = randomGenerator.nextInt(guns.size());
    return guns.get(index);
  }

  public void handleTargetingAndFire(ScannedRobotEvent e) {
    //this is here for the abstract
  }
  public void handleTargeting(ScannedRobotEvent e) {
    this.e = e;
    if (currentGun == null) {
        //set the gun
        currentGun = getRandomGun();
    }
    if (p.getGunHeat() > 0) {//cannot fire
        ///get the targeter to keep the turret on target
        currentGun.handleTargeting(e);
    }
    if (p.getGunHeat() == 0) {
        //now let the target fire
        currentGun.handleTargetingAndFire(e);
        //and tell this gun to select the next gun
        currentGun = null;
    }
  }
  public void handleTargeting() {
    Gun gun = getRandomGun();
    gun.handleTargeting(e);
    p.out.println("GUN = "+gun);
  }
}
