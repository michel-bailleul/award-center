package misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;


public class MonDialogue extends JDialog {

  // les donnees mises a jour par la boite de dialogue

  Integer lEntier = new Integer(0);

  String leNom;



  public MonDialogue(JFrame f, String titre, boolean modal) {

      super(f,titre, modal);

      try {
         jbInit();
      }
      catch(Exception e) {
         e.printStackTrace();
      }

  }



 // initialise les valeurs de la boite de dialogue

 // avec les valeurs contenues dans le tableau

  public void initialise( Object [] o){

     lEntier = (Integer)o[0];
     leNom = (String)o[1];
     jTextField1.setText(lEntier.toString());
     jTextField2.setText(leNom);

  }



  public void show(Object [] o){

      initialise(o);
      show();

  }

 // recupere les donnees mises a jour par la boite de dialogue

  public Object [] getDonnees(){

     return new Object[]{lEntier,leNom};

  }

 // les methodes de reponse aux boutons OK et Cancel

  void jButtonOK_actionPerformed(ActionEvent e) {

   messages.setText(" ");

   try {
      lEntier = new Integer(jTextField1.getText());
      leNom = jTextField2.getText();
      hide();
   }
   catch(NumberFormatException nfe){
      messages.setText("entrer un entier !");
   }

  }



  void jButtonCANCEL_actionPerformed(ActionEvent e) {

    messages.setText(" ");
    hide();

  }



 // l'interface

  public MonDialogue() {

   try {
     jbInit();
   }
   catch(Exception e) {
     e.printStackTrace();
   }

  }



 BorderLayout borderLayout1 = new BorderLayout();

 JPanel jPanel1 = new JPanel();

 JTextField jTextField1 = new JTextField();

 JPanel jPanel2 = new JPanel();

 JButton jButton1 = new JButton();

 JButton jButton2 = new JButton();

 GridLayout gridLayout1 = new GridLayout();

 TitledBorder titledBorder1;

 JLabel jLabel1 = new JLabel();

 JLabel jLabel2 = new JLabel();

 JTextField jTextField2 = new JTextField();

 JLabel messages = new JLabel();



 private void jbInit() throws Exception {

   titledBorder1 = new TitledBorder("");

   jTextField1.setText("jTextField1");

   jTextField1.setBounds(new Rectangle(101, 24, 92, 21));

   jButton1.setText("OK");

   jButton1.addActionListener(new java.awt.event.ActionListener() {

     @Override
    public void actionPerformed(ActionEvent e) {

       jButtonOK_actionPerformed(e);

     }

   });

   jButton2.setText("Annuler");

   jButton2.addActionListener(new java.awt.event.ActionListener() {

     @Override
    public void actionPerformed(ActionEvent e) {

       jButtonCANCEL_actionPerformed(e);

     }

   });

   jPanel2.setLayout(gridLayout1);

   gridLayout1.setRows(4);

   gridLayout1.setColumns(1);

   gridLayout1.setVgap(5);

   jPanel2.setBorder(BorderFactory.createEtchedBorder());

   jPanel1.setBorder(BorderFactory.createEtchedBorder());

   jPanel1.setPreferredSize(new Dimension(200, 1));

   jPanel1.setLayout(null);

   jLabel1.setFont(new java.awt.Font("Dialog", 0, 14));

   jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);

   jLabel1.setText("un entier");

   jLabel1.setBounds(new Rectangle(18, 24, 75, 17));

   jLabel2.setFont(new java.awt.Font("Dialog", 0, 14));

   jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);

   jLabel2.setText("un nom");

   jLabel2.setBounds(new Rectangle(18, 56, 75, 17));

   jTextField2.setText("jTextField2");

   jTextField2.setBounds(new Rectangle(101, 57, 92, 21));

   messages.setForeground(Color.red);

   messages.setText(" ");

   this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

   this.setResizable(false);

   jPanel1.add(jLabel1, null);

   jPanel1.add(jTextField1, null);

   jPanel1.add(jLabel2, null);

   jPanel1.add(jTextField2, null);

   this.getContentPane().add(messages,  BorderLayout.SOUTH);

   this.getContentPane().add(jPanel2,  BorderLayout.EAST);

   jPanel2.add(jButton1, null);

   jPanel2.add(jButton2, null);

   this.getContentPane().add(jPanel1,  BorderLayout.CENTER);

 }


}