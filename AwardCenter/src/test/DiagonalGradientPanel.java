package test;


import static javax.swing.BorderFactory.createEmptyBorder;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;


public class DiagonalGradientPanel extends JPanel {
  public static enum Flavor {
    OPAQUE, NON_OPAQUE;
  }

  int side = 25;
  GeneralPath triangle;
  Paint gradient;

  public DiagonalGradientPanel() {
    this(Flavor.OPAQUE);
  }

  public DiagonalGradientPanel(Flavor flavor) {
    triangle = new GeneralPath();
    triangle.moveTo(0,0);
    triangle.lineTo(side, 0);
    triangle.lineTo(side, side);
    triangle.closePath();
    switch (flavor) {
      case NON_OPAQUE: {
        gradient = new LinearGradientPaint(new Point(0, side), new Point(side, 0), new float[] { 0f, 0.5f, 1f }, new Color[] { new Color(0, 255, 255, 0), Color.BLACK, Color.DARK_GRAY });
        //gradient = new GradientPaint(side/2f, side/2f, Color.BLACK, side, 0, Color.DARK_GRAY);
        setOpaque(false);
      }
        break;
      case OPAQUE:
      default: {
        gradient = new LinearGradientPaint(new Point(0, side), new Point(side, 0), new float[] { 0f, 0.5f, 1f }, new Color[] { Color.DARK_GRAY, Color.BLACK, Color.DARK_GRAY });
        //gradient = new GradientPaint(side/2f, side/2f, Color.BLACK, side, 0, Color.DARK_GRAY);
      }
    }
  }

  /** {@inheritDoc}
   */
  @Override protected void paintComponent(Graphics graphics) {
    /*
    Dimension size = getSize();
    Insets insets = getInsets();
    int width = size.width - (insets.left + insets.right);
    int height = size.height - (insets.top + insets.bottom);
    if ((width > 0) && (height > 0)) {
      Graphics2D g2d = (Graphics2D) graphics.create();
      try {
        double sx = width / (double) side;
        double sy = height / (double) side;
        g2d.setPaint(gradient);
        g2d.scale(sx, sy);
        g2d.fillRect(0, 0, side, side);
      }
      finally {
        g2d.dispose();
      }
    }
    */
    Dimension size = getSize();
    Insets insets = getInsets();
    int width = size.width - (insets.left + insets.right);
    int height = size.height - (insets.top + insets.bottom);
    Graphics2D g2d = (Graphics2D) graphics.create();
    try {
      double sx = width / (double) side;
      double sy = height / (double) side;
      g2d.setPaint(gradient);
      g2d.scale(sx, sy);
      g2d.fill(triangle);
      AffineTransform transform = AffineTransform.getRotateInstance(Math.PI, side/2d, side/2d);
      g2d.transform(transform);
      g2d.fill(triangle);
    }
    finally {
      g2d.dispose();
    }
  }

  /** Self-test main.
   * @param args Arguments from the command-line.
   * @throws Exception In case of error.
   */
  public static void main(String... args) throws Exception {
    final int GAP = 10;
    System.out.println(System.getProperty("java.version"));
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        DiagonalGradientPanel diag1 = new DiagonalGradientPanel(Flavor.OPAQUE);
        DiagonalGradientPanel diag2 = new DiagonalGradientPanel(Flavor.NON_OPAQUE);
        JFrame frame = new JFrame("Test ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        diag1.setLayout(new BorderLayout(GAP, GAP));
        diag1.setBorder(createEmptyBorder(GAP, GAP, GAP, GAP));
        frame.add(diag1);
        frame.add(diag2);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });
  }

}