package cmc.shapes;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Shape {
  public Shape() {
  }
  protected int ticks=0;
  public void setTicks(int t) {
    ticks=t;
  }
  public int getTicks() {
    return ticks;
  }
  abstract public void draw(Graphics2D g);
}
