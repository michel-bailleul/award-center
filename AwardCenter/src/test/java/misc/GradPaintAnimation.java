package misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * GRADIENT PAINT VISUALISATION
 *
 * @author Gulam-H - ghmoledina@hotmail.com
 * @version 1.0 Created on:2005-05-29
 */
public class GradPaintAnimation extends JPanel implements MouseListener,
    MouseWheelListener {
  private int width;

  private int height;

  private int x1;

  private int y1;

  private int x2;

  private int y2;

  private Timer timer;

  private boolean isCyclic = false;

  private boolean doX = true;

  private boolean drawControlPoints = true;

  private double x1D;

  private double y1D;

  private int timerSpeed = 500;

  private JButton styleButton;

  private JButton animationButton;

  private JButton pauseUnpause;

  private JButton increaseSpeed;

  private JButton decreaseSpeed;

  private JButton drawUndrawControlPoints;

  private JButton color1B;

  private JButton color2B;

  private Color color1 = Color.orange;

  private Color color2 = Color.yellow;

  private Color point1Color = Color.black;

  private Color point2Color = Color.black;

  private JTextField valeurX1;

  private JTextField valeurY1;

  private JTextField valeurX2;

  private JTextField valeurY2;

  private JPanel valeurCoord;

  private int animationCounter = 1;

  private int numberOfAnimations = 3;

  /*
   * Le main cree un JFrame et utilise le JPanel qui contient la totalite du
   * programme. Ce code est donc reutilisable.
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setSize(900, 600);
    frame.setTitle("GRADIENT PAINT VISUALISATION");
    frame.setResizable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // On recupere la taille de l'ecran
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // On y place notre fenetre aux environs du centre de l'ecran
    frame.setLocation(screenSize.width / 15, screenSize.height / 12);

    GradPaintAnimation example = new GradPaintAnimation();
    frame.getContentPane().add(example);
    frame.setJMenuBar(example.getMenuBar());
    frame.setVisible(true);
    // frame.pack();
  }

  /**
   * Constructeur Charge toutes les compasantes de l'animation
   */
  public GradPaintAnimation() {
    super();
    this.addMouseListener(this);
    this.addMouseWheelListener(this);
    JPanel optionPanel = new JPanel();
    optionPanel.setLayout(new GridLayout(2, 1));
    JPanel colorChooserPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel coordPanel = new JPanel();
    JPanel colorAndCoord = new JPanel();

    this.setLayout(new BorderLayout());

    setDoubleBuffered(true);

    color1B = new JButton("Color 1");
    color1B.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg) {
        askColor((short) 1);
      }
    });
    colorChooserPanel.add(color1B);
    color1B.setBackground(color1);

    color2B = new JButton("Color 2");
    color2B.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        askColor((short) 2);
      }
    });
    colorChooserPanel.add(color2B);
    color2B.setBackground(color2);

    colorChooserPanel.setOpaque(false);

    colorChooserPanel
        .setBorder(BorderFactory.createTitledBorder(BorderFactory
            .createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
            "Colors"));

    valeurX1 = new JTextField();
    valeurX1.setText("" + x1);
    valeurX1.setEditable(false);

    valeurY1 = new JTextField();
    valeurY1.setText("" + y1);
    valeurY1.setEditable(false);

    valeurX2 = new JTextField();
    valeurX2.setText("" + x2);
    valeurX2.setEditable(false);

    valeurY2 = new JTextField();
    valeurY2.setText("" + y2);
    valeurY2.setEditable(false);

    coordPanel.setLayout(new GridLayout(2, 4));
    coordPanel.setOpaque(true);
    coordPanel.add(new JLabel(" X1 :"));
    coordPanel.add(valeurX1);
    coordPanel.add(new JLabel(" X2 :"));
    coordPanel.add(valeurX2);
    coordPanel.add(new JLabel(" Y1 :"));
    coordPanel.add(valeurY1);
    coordPanel.add(new JLabel(" Y2 :"));
    coordPanel.add(valeurY2);

    coordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
        .createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
        "Control Points"));

    coordPanel.setOpaque(false);

    colorAndCoord.setOpaque(false);
    colorAndCoord.setLayout(new GridLayout(1, 2));
    colorAndCoord.add(colorChooserPanel);
    colorAndCoord.add(coordPanel);

    animationButton = new JButton("Change Animation");
    animationButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        changeAnimation();
      }
    });
    animationButton.setIcon(new ImageIcon("icons/animation.png"));
    buttonPanel.add(animationButton);

    styleButton = new JButton("Cyclic");
    styleButton.setIcon(new ImageIcon("icons/cyclic.png"));
    styleButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        changeStyle();
      }
    });

    buttonPanel.add(styleButton);
    buttonPanel.setOpaque(false);

    pauseUnpause = new JButton("Pause");
    pauseUnpause.setIcon(new ImageIcon("icons/pause.png"));
    pauseUnpause.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        pauseUnpause();
      }
    });
    buttonPanel.add(pauseUnpause);

    increaseSpeed = new JButton("Increase Speed");
    increaseSpeed.setIcon(new ImageIcon("icons/increase.png"));
    increaseSpeed.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        increaseTimerSpeed();
      }
    });
    buttonPanel.add(increaseSpeed);

    decreaseSpeed = new JButton("Decrease Speed");
    decreaseSpeed.setIcon(new ImageIcon("icons/decrease.png"));
    decreaseSpeed.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        decreaseTimerSpeed();
      }
    });
    buttonPanel.add(decreaseSpeed);

    drawUndrawControlPoints = new JButton("Undraw Control Points");
    drawUndrawControlPoints.setIcon(new ImageIcon("icons/undraw.png"));
    drawUndrawControlPoints.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        drawUndrawPoints();
        repaint();
      }
    });
    buttonPanel.add(drawUndrawControlPoints);
    buttonPanel.setBorder(BorderFactory
        .createTitledBorder(BorderFactory
            .createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
            "Options"));

    optionPanel.add(colorAndCoord);
    optionPanel.add(buttonPanel);
    optionPanel.setOpaque(false);

    this.add(optionPanel, BorderLayout.SOUTH);

    timer = new Timer(timerSpeed, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        switch (animationCounter) {
          case 1: {
            animate();
            break;
          }
          case 2: {
            animate2();
            break;
          }
          case 3: {
            animate3();
            break;
          }
          default:
            break;
        }
      }

    });

    x1D = y1D = x1 = y1 = 300;
    x2 = y2 = 310;

    timer.start();
  }

  /**
   * Fonction qui retourne un JMenuBar pour ce logiciel
   *
   * @return
   */
  private JMenuBar getMenuBar() {
    JMenuBar menu = new JMenuBar();

    JMenu main = new JMenu("Main");

    final JMenuItem help = new JMenuItem("Help");
    main.add(help);
    final JMenuItem version = new JMenuItem("Version");
    main.add(version);
    final JMenuItem close = new JMenuItem("Close");
    main.add(close);

    JMenu animation = new JMenu("Animation");

    final JMenuItem changAnimation = new JMenuItem("Next Animation");
    animation.add(changAnimation);
    final JMenuItem cyclicAcyclic = new JMenuItem("Cyclic / Acyclic");
    animation.add(cyclicAcyclic);
    final JMenuItem pausePlay = new JMenuItem("Play / Pause");
    animation.add(pausePlay);
    final JMenuItem incSpeed = new JMenuItem("Increase Speed");
    animation.add(incSpeed);
    final JMenuItem decSpeed = new JMenuItem("Decrease Speed");
    animation.add(decSpeed);
    final JMenuItem drawUndraw = new JMenuItem("Draw / Undraw Control Points");
    animation.add(drawUndraw);

    JMenu changColor = new JMenu("Colors");

    final JMenuItem changCol1 = new JMenuItem("Change color 1");
    changColor.add(changCol1);
    final JMenuItem changCol2 = new JMenuItem("Change color 2");
    changColor.add(changCol2);

    menu.add(main);
    menu.add(animation);
    menu.add(changColor);

    ActionListener actLis = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg) {
        Object ob = arg.getSource();
        if (ob == help) {
          showHelp();
        } else if (ob == version) {
          showVersion();
        } else if (ob == close) {
          System.exit(0);
        } else if (ob == incSpeed) {
          increaseTimerSpeed();
        } else if (ob == decSpeed) {
          decreaseTimerSpeed();
        } else if (ob == changAnimation) {
          changeAnimation();
        } else if (ob == pausePlay) {
          pauseUnpause();
        } else if (ob == cyclicAcyclic) {
          changeStyle();
        } else if (ob == drawUndraw) {
          drawUndrawPoints();
        } else if (ob == changCol1) {
          askColor((short) 1);
        } else if (ob == changCol2) {
          askColor((short) 2);
        }
      }
    };

    help.addActionListener(actLis);
    version.addActionListener(actLis);
    close.addActionListener(actLis);
    incSpeed.addActionListener(actLis);
    decSpeed.addActionListener(actLis);
    changAnimation.addActionListener(actLis);
    pausePlay.addActionListener(actLis);
    cyclicAcyclic.addActionListener(actLis);
    drawUndraw.addActionListener(actLis);
    changCol1.addActionListener(actLis);
    changCol2.addActionListener(actLis);

    return menu;
  }

  /**
   * Procedure appele a chaque repaint Dessine l'animation
   */
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    width = this.getWidth();
    height = this.getHeight();

    Rectangle inside = new Rectangle(0, 0, width, height);
    g2.setPaint(getGradientPaint());
    g2.fill(inside);

    valeurX1.setText("" + x1);
    valeurY1.setText("" + y1);
    valeurX2.setText("" + x2);
    valeurY2.setText("" + y2);

    if (drawControlPoints) {
      g2.setColor(point1Color);
      g2.drawString("1", x1, y1);
      Rectangle point1 = new Rectangle(x1, y1, 2, 2);
      g2.setPaint(new GradientPaint(x1, y1, point1Color, x2, y2, point1Color,
          false));
      g2.fill(point1);

      g2.setColor(point2Color);
      g2.drawString("2", x2, y2);
      Rectangle point2 = new Rectangle(x2, y2, 2, 2);
      g2.setPaint(new GradientPaint(x2, y2, point2Color, x2, y2, point2Color,
          false));
      g2.fill(point2);
    }
  }

  /**
   * Fonction qui retourne le gradientPaint selon les parametres
   *
   * @return
   */
  private GradientPaint getGradientPaint() {
    return new GradientPaint(x1, y1, color1, x2, y2, color2, isCyclic);
  }

  /**
   * Procedure qui affiche le JColorChooser et change la couleur selon le choix
   * de l'utilisateur
   *
   * @param s
   *          : 1 ou 2 ( changer couleur1 ou couleur2 )
   */
  private void askColor(final short s) {
    final JDialog d = new JDialog();
    d.setLayout(new BorderLayout());
    final JColorChooser j = new JColorChooser();
    j.setVisible(true);
    d.add(j, BorderLayout.CENTER);
    d.setVisible(true);
    d.setSize(460, 380);
    d.setLocation(150, 150);

    JPanel buttonPanel = new JPanel();

    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        Color c = j.getColor();
        if ((s == 1) && (c != null)) {
          color1 = c;
          color1B.setBackground(c);
          checkPointsColor();
        } else if ((s == 2) && (c != null)) {
          color2 = c;
          color2B.setBackground(c);
          checkPointsColor();
        }
        d.setVisible(false);
        repaint();
        return;
      }
    });
    buttonPanel.add(ok);

    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        d.setVisible(false);
        return;
      }
    });

    buttonPanel.add(cancel);
    d.add(buttonPanel, BorderLayout.SOUTH);

    d.pack();
  }

  /**
   * Animation 1 - sans commentaire - je ne sais comment expliquer l'effet
   */
  private void animate() {
    /*
     * Seul un des des points bougent a chaque appel Seul l'abscisse ou
     * l'ordonnee change ( pas les 2 ) Soit on incremente, soit on decremente
     */
    int diff = 1;
    int tmp = (int) (Math.random() + 0.5999);
    int tmp2 = (int) (Math.random() + 0.5999);
    int tmp3 = (int) (Math.random() + 0.5999);

    if (tmp == 0) {
      if (tmp2 == 0) {
        if (tmp3 == 0) {
          x1 += diff;
        } else
          x1 -= diff;
      } else {
        if (tmp3 == 0) {
          y1 += diff;
        } else
          y1 -= diff;
      }
    } else {
      if (tmp2 == 0) {
        if (tmp3 == 0) {
          x2 += diff;
        } else
          x2 -= diff;
      } else {
        if (tmp3 == 0) {
          y2 += diff;
        } else
          y2 -= diff;
      }
    }
    repaint();
  }

  /**
   * Animation 2 - le point1 tourne autour du point2 en 4 phases Pas beau a voir
   * cote rotation Je ne trouve pas la formule pour la rotation autour d'un
   * point quelconque ( et non le centre )
   */
  private void animate2() {
    // la distance entre les deux points
    int k = 5;

    if (doX) {
      if (x1 < x2) {
        x1 = x2 + k;
      } else if (x1 >= x2) {
        x1 = x2 - k;
      }
      doX = false;
    } else {
      if (y1 < y2) {
        y1 = y2 + k;
      } else if (y1 >= y2) {
        y1 = y2 - k;
      }
      doX = true;
    }
    repaint();
  }

  /**
   * Animation 3 - point2 est sur (0,0) et le point1 tourne autour
   *
   */
  private void animate3() {

    /*
     * Formule de rotation autour du centre (0,0) : x' = xcos(theta) +
     * ysin(theta) y' = -xsin(theta) + ycos(theta)
     */

    /*
     * on travaille avec des double, mais on doit avoir des int pour dessiner
     */
    double theta = Math.toRadians(25);
    double cosT = Math.cos(theta);
    double sinT = Math.sin(theta);

    double x1Tmp = x1D;
    double y1Tmp = y1D;

    x1D = x1Tmp * cosT + y1Tmp * sinT;
    y1D = y1Tmp * cosT - x1Tmp * sinT;

    x1 = (int) x1D;
    y1 = (int) y1D;

    repaint();
  }

  /**
   * Procedure appellee lors d'un click de la souris sur la zone de l'animation
   * Le click gauche fixe le point de control 1 Le click droit fixe le point de
   * control 2 Et le click avec la roulette permet de rendre cyclique/acyclique
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
      x1D = x1 = e.getX();
      y1D = y1 = e.getY();
      valeurX1.setText("" + x1);
      valeurY1.setText("" + y1);
      repaint();
    } else if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
      x2 = e.getX();
      y2 = e.getY();
      valeurX2.setText("" + x2);
      valeurY2.setText("" + y2);
      repaint();
    } else {
      changeStyle();
    }
  }

  // pour le futur
  @Override
  public void mousePressed(MouseEvent arg0) {
  }

  @Override
  public void mouseReleased(MouseEvent arg0) {
  }

  @Override
  public void mouseEntered(MouseEvent arg0) {
  }

  @Override
  public void mouseExited(MouseEvent arg0) {
  }

  /**
   * Procedure appelee lors d'un evenement avec la roulette de la souris Permet
   * d'acceler/ralentir l'animation selon le sens de la rotation
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    int wheelRotat = e.getWheelRotation();
    if (wheelRotat == -1) {
      increaseTimerSpeed();
    } else {// wheelRotat==-1
      decreaseTimerSpeed();
    }
  }

  /**
   * Procedure qui permet de passer a l'animation suivante
   */
  private void changeAnimation() {
    if (timer.isRunning()) {
      animationCounter++;
      if (animationCounter > numberOfAnimations)
        animationCounter = 1;
      switch (animationCounter) {
        case 1: {
          x1D = y1D = x1 = y1 = 300;
          x2 = y2 = 310;
          break;
        }
        case 2: {
          x1D = y1D = x1 = y1 = 300;
          x2 = y2 = 310;
          break;
        }
        case 3: {
          x1D = y1D = x1 = y1 = 25;
          x2 = y2 = 3;
          break;
        }
        default: {
          break;
        }
      }
    }
  }

  /**
   * Procedure qui rend l'animation cyclique/acyclique et change le texte du
   * boutton en consequence
   */
  private void changeStyle() {
    isCyclic = !isCyclic;
    if (isCyclic) {
      styleButton.setText("Acyclic");
      styleButton.setIcon(new ImageIcon("icons/acyclic.png"));
    } else {
      styleButton.setText("Cyclic");
      styleButton.setIcon(new ImageIcon("icons/cyclic.png"));
    }
    repaint();
  }

  /**
   * Procedure qui accelere l'animation et active/desactive les boutons en en
   * question si on peut toujours ralentir ou acceler l'animation
   */
  private void increaseTimerSpeed() {
    if (timerSpeed > 60) {
      timerSpeed -= 50;
      decreaseSpeed.setEnabled(true);
    } else {
      timerSpeed = 10;
      increaseSpeed.setEnabled(false);
    }
    timer.setDelay(timerSpeed);
  }

  /**
   * Procedure qui ralenti l'animation et active/desactive les boutons en en
   * question si on peut toujours ralentir ou acceler l'animation
   */
  private void decreaseTimerSpeed() {
    if (timerSpeed < 1950) {
      timerSpeed += 50;
      increaseSpeed.setEnabled(true);
    } else {
      timerSpeed = 2000;
      decreaseSpeed.setEnabled(false);
    }
    timer.setDelay(timerSpeed);
  }

  /**
   * Procedure qui pause et unpause l'animation et modifie le texte du boutton
   * en consequence
   */
  private void pauseUnpause() {
    if (timer.isRunning()) {
      timer.stop();
      pauseUnpause.setText("Play");
      pauseUnpause.setIcon(new ImageIcon("icons/play.png"));
      animationButton.setEnabled(false);
    } else {
      timer.start();
      pauseUnpause.setText("Pause");
      pauseUnpause.setIcon(new ImageIcon("icons/pause.png"));
      animationButton.setEnabled(true);
    }
  }

  /**
   * Procedure qui 'set' si on doit dessiner les pts de control ou pas et
   * modifie le texte du boutton en consequence
   */
  private void drawUndrawPoints() {
    if (drawControlPoints) {
      drawControlPoints = false;
      drawUndrawControlPoints.setText("Draw Points");
      drawUndrawControlPoints.setIcon(new ImageIcon("icons/draw.png"));
    } else {
      drawControlPoints = true;
      drawUndrawControlPoints.setText("Undraw Points");
      drawUndrawControlPoints.setIcon(new ImageIcon("icons/undraw.png"));

      checkPointsColor();
    }
  }

  /**
   * Procedure qui set la couleur des points de controles selon la couleur de
   * l'animation (soit blanc, soit noir)
   */
  private void checkPointsColor() {
    // decalage
    int gamma = 52;

    // pour le point (x1, y1)
    if (color1.getGreen() < Color.black.getGreen() + gamma) {
      if (color1.getRed() < Color.black.getRed() + gamma) {
        if (color1.getBlue() < Color.black.getBlue() + gamma) {
          // si la couleur est proche du noir, le pt de control en blanc
          point1Color = Color.white;
        }
      }
    } else
      // sinon le pt de control en noir
      point1Color = Color.black;

    // de meme pour le point (x2,y2)
    if (color2.getGreen() < Color.black.getGreen() + gamma) {
      if (color2.getRed() < Color.black.getRed() + gamma) {
        if (color2.getBlue() < Color.black.getBlue() + gamma) {
          point2Color = Color.white;
        }
      }
    } else
      point2Color = Color.black;
  }

  /**
   * Procedure qui affiche en pop-up les infos sur la version du programme
   */
  private void showVersion() {
    JDialog.setDefaultLookAndFeelDecorated(false);
    final JDialog d = new JDialog();
    d.setResizable(false);
    d.setTitle("Version");
    d.setSize(275, 250);
    d.setLocation((int) this.getLocationOnScreen().getX() + 350, (int) this
        .getLocationOnScreen().getY() + 100);

    d.setLayout(new BorderLayout());

    String text = " GRADIENT PAINT VISUALISATION\n\n" + " Author : Gulam-H\n"
        + " Email : ghmoledina@hotmail.com\n\n" + " Version : 1.0\n\n"
        + "If this program was helpfull, please\n"
        + "donate some money to the author.\n"
        + "You can send your donation to the author's\n"
        + "email adress with PayPal (www.paypal.com)\n" + "\n";

    final JTextArea textArea = new JTextArea(text, 3, 3);
    textArea.setMargin(new Insets(5, 5, 5, 5));
    textArea.setEditable(false);
    d.add(textArea, BorderLayout.CENTER);

    final JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        d.setVisible(false);
      }
    });
    d.add(ok, BorderLayout.SOUTH);
    d.setVisible(true);
  }

  /**
   * Procedure qui affiche en pop-up l'aide sur le programme
   */
  private void showHelp() {
    JDialog.setDefaultLookAndFeelDecorated(false);
    final JDialog d = new JDialog();
    d.setTitle("Help");
    d.setSize(334, 269);
    d.setResizable(false);
    d.setLocation((int) this.getLocationOnScreen().getX() + 350, (int) this
        .getLocationOnScreen().getY() + 100);

    d.setLayout(new BorderLayout());

    String text = " GRADIENT PAINT VISUALISATION HELP\n\n"
        + " Version : 1.0\n\n"
        + " 1-/ Use the menu and the control panel to change options\n"
        + " 2-/ Click on the panel to change points positions :\n"
        + " - left click : to change the position of Point 1\n"
        + " - right click : to change the position of Point 2\n"
        + " 3-/ You can also use some mouse shortcuts : \n"
        + " - wheel click : to switch to cyclic/acyclic style\n"
        + " - wheel up : to increase animation speed\n"
        + " - wheel down : to decrease animation speed\n" + " \n";

    final JTextArea textArea = new JTextArea(text, 3, 3);
    textArea.setMargin(new Insets(5, 5, 5, 5));
    textArea.setEditable(false);
    d.add(textArea, BorderLayout.CENTER);

    final JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        System.out.println(d.getSize());
        d.setVisible(false);
      }
    });
    d.add(ok, BorderLayout.SOUTH);
    d.setVisible(true);
  }

}
