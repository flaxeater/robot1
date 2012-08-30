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
  double oldEnemyHeading;
  private ArrayList<Gun> guns = new ArrayList<Gun>();
  private Random randomGenerator = new Random();

  public VirtualGunTargeting(RadionAtascar parent) {
    super(parent);
    oldEnemyHeading = 0;
    //Add the guns
    guns.add(new RandomTargeting(p));
    guns.add(new CircularTargeting(p));
    guns.add(new QuadraticLinearTargeting(p));
    guns.add(new HeadOnTargeting(p));
    guns.add(new HeadFakeTargeting(p));
  }

  public Gun getRandomGun() {
    int index = randomGenerator.nextInt(guns.size());
//    return guns.get(index);
    return guns.get(4);
  }

  public void handleTargeting(ScannedRobotEvent e) {
    this.e = e;
//    setMyCoords();
    handleTargeting();
  }
  public void handleTargeting() {
    p.out.println("VIRTUAL GUN");
    Gun gun = getRandomGun();
    gun.handleTargeting(e);
    p.out.println("GUN = "+gun);
  }
}
