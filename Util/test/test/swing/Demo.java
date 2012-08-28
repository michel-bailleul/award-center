package test.swing;
import javax.swing.*;

import util.resources.SwingLogKey;

import util.resource.ResourceUtil;

import java.awt.event.*;
import java.awt.*;

public class Demo extends JTextField
{

  public Demo()
  {

    JFrame frame = new JFrame("Demo");
    Container pane = frame.getContentPane();
    JPanel panel = new JPanel();

//    JTextField tf = new JTextField(20);
    JTextField tf = new JSearchTextField(10);
    JTextField tf0 = new JSearchTextField(10);
    panel.add(tf);
    panel.add(tf0);

//    JTextField usernameField = new JTextFieldHint(new JTextField(10),"user_green","Username");
//    JTextField passwordField = new JTextFieldHint(new JPasswordField(10),"bullet_key","Password");
//    panel.add(usernameField);
//    panel.add(passwordField);


    pane.add(panel, BorderLayout.NORTH);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300,200);
    frame.setVisible(true);


  }

  @Override
  public void paintComponent(Graphics g)
  {
    //to fill in
  }

  public static void main(String[] args)
  {
    new Demo();
  }

}
