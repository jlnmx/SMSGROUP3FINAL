package sms2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLogin extends JFrame implements ActionListener {
    private JLabel lblTitle, lblUsername, lblPassword,bg;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnBack;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public AdminLogin() {
        setTitle("Admin Login");
        setLayout(null);
        
        
        getContentPane().setBackground(new Color(245, 245, 220));
        
        bg = new JLabel();
        bg.setIcon (new ImageIcon(new ImageIcon( "C:\\Users\\saban\\Documents\\NetBeansProjects\\OOP\\src\\SMSGROUP3FINAL\\SMSFINAL\\src\\sms2\\puplogo.png").getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH)));
        bg.setBounds(30, 20, 125, 125);
        

        lblTitle = new JLabel("Admin Log In");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 40));
        lblTitle.setBounds(210, 170, 250, 55);
        lblTitle.setForeground(new Color(128, 0, 0));

        lblUsername = new JLabel("Username:");
        lblUsername.setBounds(130, 250, 100, 25);
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 18));

        lblPassword = new JLabel("Password:");
        lblPassword.setBounds(130, 300, 100, 25);
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        txtUsername = new JTextField();
        txtUsername.setBounds(280, 250, 200, 30);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 18));

        txtPassword = new JPasswordField();
        txtPassword.setBounds(280, 300, 200, 30);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        btnLogin = new JButton("Login");
        btnLogin.setBounds(200, 400, 100, 40);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
        btnLogin.setBackground(new Color(128, 0, 0)); 
        btnLogin.setForeground(Color.WHITE); 
        btnLogin.addActionListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(350, 400, 100, 40);
        btnBack.setFont(new Font("Arial", Font.BOLD, 18));
        btnBack.setBackground(new Color(128, 0, 0)); 
        btnBack.setForeground(Color.WHITE); 
        btnBack.addActionListener(this);

        
        
        add(lblTitle);
        add(lblUsername);
        add(lblPassword);
        add(txtUsername);
        add(txtPassword);
        add(btnLogin);
        add(btnBack);
        add(bg);
        
        
        setSize(700, 600);
        setLocationRelativeTo(null);
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
            new MainMenu().setVisible(true);
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
