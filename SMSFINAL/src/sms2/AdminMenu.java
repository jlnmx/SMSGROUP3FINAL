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
        
        lblTitle = new JLabel("Administrator Menu", SwingConstants.CENTER);
        lblTitle.setBounds(100, 50, 400, 40);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(128, 0, 0)); 

        btnAD = new JButton("Admin Dashboard");
        btnAD.setBounds(150, 120, 300, 40);
        btnAD.setBackground(new Color(128, 0, 0)); 
        btnAD.setForeground(Color.WHITE);       
        btnAD.setFont(new Font("Arial", Font.BOLD, 18));

        btnVAttnd = new JButton("Attendance");
        btnVAttnd.setBounds(150, 200, 300, 40);
        btnVAttnd.setBackground(new Color(128, 0, 0)); 
        btnVAttnd.setForeground(Color.WHITE);
        btnVAttnd.setFont(new Font("Arial", Font.BOLD, 18));
        
        btnVGrds = new JButton("Grades");
        btnVGrds.setBounds(150, 280, 300, 40);
        btnVGrds.setBackground(new Color(128, 0, 0)); 
        btnVGrds.setForeground(Color.WHITE);
        btnVGrds.setFont(new Font("Arial", Font.BOLD, 18));
        
        btnHome = new JButton("Return");
        btnHome.setBounds(520, 20, 100, 30);
        btnHome.setBackground(new Color(128, 0, 0)); 
        btnHome.setForeground(Color.WHITE);
        btnHome.setFont(new Font("Arial", Font.PLAIN, 14));
        
        setResizable(false);
        setSize(650, 500);
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
            SwingUtilities.invokeLater(() -> new GradesDashboard());
        }
        dispose();
    }

    public static void main(String[] args) {
        new AdminMenu();
    }
}
