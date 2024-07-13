package sms2;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu extends JFrame implements ActionListener {
    private JLabel lblTitle;
    private JButton btnAD, btnHome, btnVAttnd, btnVGrds;

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

        btnVAttnd = new JButton("Attendance");
        btnVAttnd.setBounds(150, 140, 150, 30);
        btnVAttnd.setBackground(new Color(128, 0, 0)); 
        btnVAttnd.setForeground(Color.WHITE);
        
        btnVGrds = new JButton("Grades");
        btnVGrds.setBounds(150, 200, 150, 30);
        btnVGrds.setBackground(new Color(128, 0, 0)); 
        btnVGrds.setForeground(Color.WHITE);
        
         
        btnHome = new JButton("Return");
        btnHome.setBounds(390, 10, 80, 30);
        btnHome.setBackground(new Color(128, 0, 0)); 
        btnHome.setForeground(Color.WHITE);
        
        setResizable(false);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        add(btnAD);
        add(lblTitle);
        add(btnHome);
        add(btnVGrds);
        add(btnVAttnd);
        
        btnAD.addActionListener(this);
        btnHome.addActionListener(this);
        btnVAttnd.addActionListener(this);
        btnVGrds.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnAD) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
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
