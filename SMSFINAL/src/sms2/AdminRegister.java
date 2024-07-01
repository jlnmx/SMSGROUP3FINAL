package sms2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminRegister extends JFrame implements ActionListener {
    private JTextField txtfldUsername;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton btnRegister, btnClear, btnBack;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public AdminRegister() {
        setTitle("Admin Registration");
        setLayout(null);

        JLabel lblTitle = new JLabel("Admin Registration");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setBounds(120, 30, 400, 40);
        add(lblTitle);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 100, 150, 30);
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        add(lblUsername);

        txtfldUsername = new JTextField();
        txtfldUsername.setBounds(250, 100, 200, 30);
        txtfldUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        add(txtfldUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 150, 150, 30);
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(250, 150, 200, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        add(passwordField);

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setBounds(50, 200, 200, 30);
        lblConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        add(lblConfirmPassword);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(250, 200, 200, 30);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 18));
        add(confirmPasswordField);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(50, 270, 100, 40);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegister.addActionListener(this);
        add(btnRegister);

        btnClear = new JButton("Clear");
        btnClear.setBounds(370, 270, 80, 40);
        btnClear.setFont(new Font("Arial", Font.BOLD, 12));
        btnClear.addActionListener(this);
        add(btnClear);

        btnBack = new JButton("Back");
        btnBack.setBounds(480, 270, 80, 40);
        btnBack.setFont(new Font("Arial", Font.BOLD, 12));
        btnBack.addActionListener(this);
        add(btnBack);

        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new MainMenu().setVisible(true);
        } else if (e.getSource() == btnClear) {
            clearFields();
        } else if (e.getSource() == btnRegister) {
            registerAdmin();
        }
    }

    private void clearFields() {
        txtfldUsername.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void registerAdmin() {
        String query = "INSERT INTO admin (username, password) VALUES (?, ?)";
        
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            String username = txtfldUsername.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Admin Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();  
            new AdminMenu().setVisible(true);  
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AdminRegister().setVisible(true);
    }
}
