package cmc.shapes;
import java.awt.Color;
import java.awt.Graphics2D;

public class Square extends Shape {
  private int x;
  private int y;
  private int l;
  /**
    @param int x, xcord
    @param int y, ycord
    @param int l, length
    @param int t, ticks to be drawn
  */
  public Square(int x,int y, int l, int t) {
    super();
    this.x = x;
    this.y = y;
    this.l = l;
    this.ticks = t;
  }
  public void draw(Graphics2D g) {
    if (ticks >0) {
      g.drawRect(x,y,l,l);
      ticks--;
    }
  }
}
