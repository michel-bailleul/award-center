package util.swing;


import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import util.swing.component.GradientPanel;


@SuppressWarnings("serial")
public class TestGradientPanel {


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