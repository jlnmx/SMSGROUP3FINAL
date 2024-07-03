package sms2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;

public class StudentMenu extends JFrame implements ActionListener {
    private JLabel lblWelcome, bg;   
    private JButton btnDashboard, btnInfo, btnBack, btnGradesSummary;

    StudentMenu() {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setTitle("Student Menu");
        
        getContentPane().setBackground(new Color(245, 245, 220));

        bg = new JLabel();
        bg.setIcon(new ImageIcon(new ImageIcon("C:\\Users\\saban\\Documents\\NetBeansProjects\\OOP\\src\\SMSGROUP3FINAL\\SMSFINAL\\src\\sms2\\puplogo.png").getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH)));
        bg.setBounds(20, 10, 125, 125);
        
        lblWelcome = new JLabel("Welcome Student");
        lblWelcome.setBounds(165, 40, 300, 20);
        lblWelcome.setFont(new Font("Bell MT", Font.BOLD, 30));
        lblWelcome.setForeground(new Color(128, 0, 0));

        btnDashboard = new JButton("View Records");
        btnDashboard.setBounds(135, 150, 300, 40);
        btnDashboard.setBackground(new Color(128, 0, 0));
        btnDashboard.setForeground(Color.WHITE);
        
        btnInfo = new JButton("Student Information");
        btnInfo.setBounds(135, 230, 300, 40);
        btnInfo.setBackground(new Color(128, 0, 0));
        btnInfo.setForeground(Color.WHITE);

        btnGradesSummary = new JButton("Grades Summary");
        btnGradesSummary.setBounds(135, 310, 300, 40);
        btnGradesSummary.setBackground(new Color(128, 0, 0));
        btnGradesSummary.setForeground(Color.WHITE);

        btnBack = new JButton("Return");
        btnBack.setBounds(490, 20, 80, 30);
        btnBack.setBackground(new Color(128, 0, 0));
        btnBack.setForeground(Color.WHITE);
        
        add(lblWelcome);
        add(btnDashboard);
        add(btnInfo);
        add(btnGradesSummary);
        add(btnBack);
        add(bg);
        
        btnDashboard.addActionListener(this);
        btnInfo.addActionListener(this);
        btnGradesSummary.addActionListener(this);
        btnBack.addActionListener(this);
        
        setVisible(true);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDashboard) {
            dispose();
            new StudentDashboard().setVisible(true);
        } else if (e.getSource() == btnInfo) {
            dispose();
            new StudentInfo().setVisible(true);
        } else if (e.getSource() == btnGradesSummary) {
            dispose();
            new GradesSummary().setVisible(true);
        } else if (e.getSource() == btnBack) {
            dispose();
            new MainMenu().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new StudentMenu();
    }
}
