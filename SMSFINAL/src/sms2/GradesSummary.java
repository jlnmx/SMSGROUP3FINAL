package sms2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GradesSummary extends JFrame implements ActionListener {
    private JTextField txtfldStudentNumber;
    private JTextArea txtaSummary;
    private JButton btnSearch, btnBack;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public GradesSummary() {
        setTitle("Student Grade Summary");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());  
        setLocationRelativeTo(null);
        
        JPanel panelInput = new JPanel(new FlowLayout());
        JLabel lblStudentNumber = new JLabel("Enter Student Number: ");
        txtfldStudentNumber = new JTextField(20);
        btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(128, 0, 0));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(this);

        panelInput.add(lblStudentNumber);
        panelInput.add(txtfldStudentNumber);
        panelInput.add(btnSearch);

        txtaSummary = new JTextArea();
        txtaSummary.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtaSummary);

        btnBack = new JButton("Back");
        btnBack.setBackground(new Color(128, 0, 0));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);

        add(panelInput, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnBack, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadGradeSummary(String studentNumber) {
        txtaSummary.setText("");
        String query = "SELECT s.surname, s.firstname, g.subject, g.midterm, g.finals, g.average " +
                       "FROM students s " +
                       "LEFT JOIN grades g ON s.studentnum = g.studentnum " +
                       "WHERE s.studentnum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder summary = new StringBuilder();
            while (rs.next()) {
                String surname = rs.getString("surname");
                String firstName = rs.getString("firstname");
                String subject = rs.getString("subject");
                double midterm = rs.getDouble("midterm");
                double finals = rs.getDouble("finals");
                double average = rs.getDouble("average");

                summary.append(String.format("Name: %s %s\n", surname, firstName));
                summary.append(String.format("Subject: %s\n", subject));
                summary.append(String.format("Midterm: %.2f\n", midterm));
                summary.append(String.format("Finals: %.2f\n", finals));
                summary.append(String.format("Average: %.2f\n", average));
                summary.append("\n--------------------------------\n\n");
            }

            if (summary.length() == 0) {
                txtaSummary.setText("No records found for student number: " + studentNumber);
            } else {
                txtaSummary.setText(summary.toString());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load grade summary: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            String studentNumber = txtfldStudentNumber.getText().trim();
            if (!studentNumber.isEmpty()) {
                loadGradeSummary(studentNumber);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a student number.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == btnBack) {
            dispose();
            new StudentMenu().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradesSummary());
    }
}
