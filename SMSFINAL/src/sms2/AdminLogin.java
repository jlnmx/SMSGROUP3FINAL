
package sms2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLogin extends JFrame implements ActionListener {
    private JLabel lblTitle, lblUsername, lblPassword;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnBack;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public AdminLogin() {
        setTitle("Admin Login");
        setLayout(null);

        lblTitle = new JLabel("Admin Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setBounds(180, 30, 200, 40);

        lblUsername = new JLabel("Username:");
        lblUsername.setBounds(100, 100, 100, 25);
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 18));

        lblPassword = new JLabel("Password:");
        lblPassword.setBounds(100, 150, 100, 25);
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        txtUsername = new JTextField();
        txtUsername.setBounds(220, 100, 200, 30);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 18));

        txtPassword = new JPasswordField();
        txtPassword.setBounds(220, 150, 200, 30);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 220, 100, 40);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
        btnLogin.addActionListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(300, 220, 100, 40);
        btnBack.setFont(new Font("Arial", Font.BOLD, 18));
        btnBack.addActionListener(this);

        add(lblTitle);
        add(lblUsername);
        add(lblPassword);
        add(txtUsername);
        add(txtPassword);
        add(btnLogin);
        add(btnBack);

        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            try {
                if (loginAdmin(username, password)) {
                    JOptionPane.showMessageDialog(this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new AdminMenu().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Login failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnBack) {
            dispose();
            new Menu().setVisible(true);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private boolean loginAdmin(String username, String password) throws SQLException {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public static void main(String[] args) {
        new AdminLogin();
    }
}
