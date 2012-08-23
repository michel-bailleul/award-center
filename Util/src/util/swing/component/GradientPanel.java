package util.swing.component;


import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import static util.resource.ResourceUtil.getMsg;
import static util.resources.SwingLogKey.GRADIENTPANEL_ILLEGALARGUMENT;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
public class GradientPanel extends JPanel {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final int side = 100;


  // —————————————————————————————————————————————————————————————— Constructors


  public GradientPanel(Color color1, Color color2) {
    this(color1, color2, 2, 0);
  }


  public GradientPanel(Color color1, Color color2, int x, int arc) {

    if (x < 2) {
      throw new IllegalArgumentException(getMsg(GRADIENTPANEL_ILLEGALARGUMENT));
    }

    this.arc = arc;

    float[] fractions = new float[x];
    Color[] colors = new Color[x];

    for (int i=0; i < x; i++) {
      fractions[i] = i*((float)1/(x - 1));
      colors[i] = (i%2 == 0) ? color1 : color2;
    }

    Point start = new Point(0, side);
    Point end   = new Point(side, 0);
    paint = new LinearGradientPaint(start, end, fractions, colors);

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private int arc = 0;

  private Paint paint;


  // ————————————————————————————————————————————————————————— Protected Methods


  /**
   * {@inheritDoc}
   */
  @Override
  protected void paintComponent(Graphics graphics) {

    Dimension size = getSize();
    Insets insets = getInsets();

    int width  = size.width  - (insets.left + insets.right);
    int height = size.height - (insets.top  + insets.bottom);

    if ((width > 0) && (height > 0)) {
      Graphics2D g2d = (Graphics2D) graphics.create();
      try {
        g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g2d.setPaint(paint);
        g2d.scale((double)width/side, (double)height/side);
        if (arc > 0) {
          int arcx=arc, arcy=arc;
          if (height > width) {
            arcy *= (double)width/height;
          }
          else {
            arcx *= (double)height/width;
          }
          g2d.fillRoundRect(0, 0, side, side, arcx, arcy);
        }
        else {
          g2d.fillRect(0, 0, side, side);
        }
      }
      finally {
        g2d.dispose();
      }
    }

  }


  // ——————————————————————————————————————————————————————————————— Main Method


  public static void main(String... args) throws Exception {

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        GradientPanel panel = new GradientPanel(Color.WHITE, Color.LIGHT_GRAY, 10, 0);
        JFrame frame = new JFrame("GradientPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });

  }


}