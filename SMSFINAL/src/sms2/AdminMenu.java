package sms2;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu extends JFrame implements ActionListener {
    private JLabel lblTitle;
    private JButton btnAD, btnAttnd, btnGrds, btnHome, btnVAttnd, btnVGrds;

    public AdminMenu() {
        setTitle("Administrator Panel");
        
        getContentPane().setBackground(new Color(245, 245, 220));
        
        lblTitle = new JLabel("Welcome Administrator", SwingConstants.CENTER);
        lblTitle.setBounds(100, 30, 250, 30);
        lblTitle.setFont(new Font("Bell MT", Font.BOLD, 20));
        lblTitle.setForeground(new Color(128, 0, 0)); 

        btnAD = new JButton("Admin Dashboard");
        btnAD.setBounds(150, 80, 150, 30);
        btnAD.setBackground(new Color(128, 0, 0)); 
        btnAD.setForeground(Color.WHITE);       

        btnAttnd = new JButton("Attendance");
        btnAttnd.setBounds(150, 140, 150, 30);
        btnAttnd.setBackground(new Color(128, 0, 0)); 
        btnAttnd.setForeground(Color.WHITE);
        
        btnGrds = new JButton("Grades");
        btnGrds.setBounds(150, 200, 150, 30);
        btnGrds.setBackground(new Color(128, 0, 0)); 
        btnGrds.setForeground(Color.WHITE);
        
        btnVAttnd = new JButton("View Attendance");
        btnVAttnd.setBounds(150, 260, 150, 30);
        btnVAttnd.setBackground(new Color(128, 0, 0)); 
        btnVAttnd.setForeground(Color.WHITE);
        
        btnVGrds = new JButton("View Grades");
        btnVGrds.setBounds(150, 320, 150, 30);
        btnVGrds.setBackground(new Color(128, 0, 0)); 
        btnVGrds.setForeground(Color.WHITE);
         
        btnHome = new JButton("Return");
        btnHome.setBounds(390, 10, 80, 30);
        btnHome.setBackground(new Color(128, 0, 0)); 
        btnHome.setForeground(Color.WHITE);
        
        setResizable(false);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        add(btnAD);
        add(lblTitle);
        add(btnHome);
        add(btnVGrds);
        add(btnGrds);
        add(btnVAttnd);
        add(btnAttnd);
        
        btnAD.addActionListener(this);
        btnAttnd.addActionListener(this);
        btnGrds.addActionListener(this);
        btnHome.addActionListener(this);
        btnVAttnd.addActionListener(this);
        btnVGrds.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnAD) {
            new AdminDashboard();
        } else if (source == btnAttnd) {
            SwingUtilities.invokeLater(() -> new Attendance(null).setVisible(true));
        } else if (source == btnGrds) {
            SwingUtilities.invokeLater(() -> new Grades().setVisible(true));
        } else if (source == btnHome) {
            new MainMenu();
        } else if (source == btnVAttnd) {
            new AttendanceDashboard();
        } else if (source == btnVGrds) {
         SwingUtilities.invokeLater(() -> {
            new GradesDashboard();});
        }
        dispose();
    }

    public static void main(String[] args) {
        new AdminMenu();
    }
}
