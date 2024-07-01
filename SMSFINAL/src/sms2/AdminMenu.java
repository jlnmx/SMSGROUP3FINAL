package sms2;

import java.awt.Font;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu extends JFrame implements ActionListener {
    private JLabel lblTitle;
    private JButton btnAD, btnAttnd, btnGrds, btnHome;

    public AdminMenu() {
        setTitle("Administrator Panel");

        lblTitle = new JLabel("Welcome Administrator", SwingConstants.CENTER);
        lblTitle.setBounds(100, 30, 250, 30); 
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle);

        btnAD = new JButton("Admin Dashboard");
        btnAD.setBounds(150, 80, 150, 30); 
        add(btnAD);

        btnAttnd = new JButton("Attendance");
        btnAttnd.setBounds(150, 140, 150, 30); 
        add(btnAttnd);

        btnGrds = new JButton("Grades");
        btnGrds.setBounds(150, 200, 150, 30);
        add(btnGrds);

        btnHome = new JButton("Return");
        btnHome.setBounds(390, 10, 80, 30); 
        add(btnHome);

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 
        setVisible(true);


        btnAD.addActionListener(this);
        btnAttnd.addActionListener(this);
        btnGrds.addActionListener(this);
        btnHome.addActionListener(this);
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
                }
                dispose();
            }

    public static void main(String[] args) {
        new AdminMenu();
    }
}
