package sms2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;

public class Homepage extends JFrame implements ActionListener{
    
   private JLabel lblTitle, bg;
   private JButton btnEnter;
    
   Homepage(){
       setTitle("Homepage");
       setLayout(null);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(600, 500);
       setResizable(false);
       setLocationRelativeTo(null);
       
        bg = new JLabel();
        bg.setIcon (new ImageIcon(new ImageIcon( "C:\\Users\\saban\\Documents\\NetBeansProjects\\OOP\\src\\SMSGROUP3FINAL\\SMSFINAL\\src\\sms2\\puplogo.png").getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH)));
        bg.setBounds(220, 20, 125, 125);
       
       lblTitle = new JLabel("Student Management System");
       lblTitle.setFont(new Font("Bell MT", Font.BOLD, 35));
       lblTitle.setBounds(60, 150, 500, 100);
       lblTitle.setForeground(new Color(128, 0, 0));

   
       btnEnter = new JButton("ENTER");
       btnEnter.setFont(new Font("Bell MT", Font.PLAIN, 25));
       btnEnter.setBounds(180, 250, 200, 50);
       btnEnter.setBackground(new Color(128, 0, 0));
       btnEnter.setForeground(Color.WHITE);
       
       getContentPane().setBackground(new Color(245, 245, 220));
       
       add(lblTitle);
       add(btnEnter);
       add(bg);
       
       btnEnter.addActionListener(this);
       setVisible(true);
   }
   @Override
   public void actionPerformed(ActionEvent e){
       if (e.getSource() == btnEnter){
           dispose();
           new MainMenu().setVisible(true);
       }
   }
   public static void main (String []args){
       new Homepage();
   }
   
}
