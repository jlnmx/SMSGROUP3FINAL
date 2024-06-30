package sms2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {
    private JLabel lblTitle, lblQuestion, lblAlreadyHaveAccount;
    private JButton btnStudent, btnAdmin, btnSignIn;

    public Menu() {
        setLayout(null);

        lblTitle = new JLabel("Student Management System");
        lblTitle.setFont(new Font("Bell MT", Font.BOLD, 24));
        lblTitle.setBounds(150, 50, 400, 60);

        lblQuestion = new JLabel("Are You A?");
        lblQuestion.setFont(new Font("Bell MT", Font.BOLD, 20));
        lblQuestion.setBounds(250, 100, 200, 60);

        btnStudent = new JButton("Student");
        btnStudent.setBounds(150, 200, 100, 50);

        btnAdmin = new JButton("Admin");
        btnAdmin.setBounds(350, 200, 100, 50);

        lblAlreadyHaveAccount = new JLabel("Already have an account?");
        lblAlreadyHaveAccount.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAlreadyHaveAccount.setBounds(400, 300, 150, 30);

        btnSignIn = new JButton("Sign In");
        btnSignIn.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSignIn.setBounds(470, 330, 80, 30);

        add(lblTitle);
        add(lblQuestion);
        add(btnStudent);
        add(btnAdmin);
        add(lblAlreadyHaveAccount);
        add(btnSignIn);

        setTitle("Student Management System");
        setSize(600, 420);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        btnStudent.addActionListener(this);
        btnAdmin.addActionListener(this);
        btnSignIn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStudent) {
            dispose();
            new StudentInfo().setVisible(true);
        } else if (e.getSource() == btnAdmin) {
            dispose();
            new AdminRegister().setVisible(true);
        } else if (e.getSource() == btnSignIn) {
            dispose();
            new AdminLogin().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Menu();
    }
}
