package sms2;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public class Homepage extends JFrame implements ActionListener{
    
   private JLabel lblTitle;
   private JButton btnEnter;
    
   Homepage(){
       setTitle("Homepage");
       setLayout(null);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(600, 500);
       setResizable(false);
       setVisible(true);
       
       lblTitle = new JLabel("Welcome to SMS");
       lblTitle.setFont(new Font("Bell MT", Font.BOLD, 50));
       lblTitle.setBounds(90, 80, 500, 100);
   
       btnEnter = new JButton("ENTER");
       btnEnter.setFont(new Font("Arial", Font.BOLD, 30));
       btnEnter.setBounds(180, 250, 200, 50);
       
       
       add(lblTitle);
       add(btnEnter);
       
       btnEnter.addActionListener(this);
   
   }
   @Override
   public void actionPerformed(ActionEvent e){
       if (e.getSource() == btnEnter){
           dispose();
           new MainMenu().setVisible(true);
       }
   }
   public static void main(String []args){
       new Homepage();
   }
}
