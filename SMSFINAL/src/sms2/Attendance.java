package sms2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Attendance extends JFrame implements ActionListener {
    private JLabel lblStatus, lblReason, lblTitle, lblSname;
    private JTextField txtfldReason;
    private JButton btnSubmit, btnBack;
    private JComboBox<String> cmbSurname;
    private JComboBox<String> cmbAttend;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";
    private AttendanceDashboard attendanceDashboard;

    public Attendance(AttendanceDashboard dashboard) {
        this.attendanceDashboard = dashboard;
        setTitle("Attendance");
        setSize(500, 400);
        setLayout(null);

        lblTitle = new JLabel("Attendance");
        lblTitle.setBounds(150, 30, 200, 20);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        lblStatus = new JLabel("Surname");
        lblStatus.setBounds(40, 70, 100, 20);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 15));

        lblSname = new JLabel("Status");
        lblSname.setBounds(40, 100, 100, 20);
        lblSname.setFont(new Font("Arial", Font.BOLD, 15));

        lblReason = new JLabel("Reason");
        lblReason.setBounds(40, 130, 100, 20);
        lblReason.setFont(new Font("Arial", Font.BOLD, 15));

        txtfldReason = new JTextField();
        txtfldReason.setBounds(120, 130, 150, 20);
        txtfldReason.setFont(new Font("Arial", Font.PLAIN, 15));

        cmbAttend = new JComboBox<>(new String[]{"Absent", "Present"});
        cmbAttend.setBounds(120, 100, 150, 20);

        cmbSurname = new JComboBox<>();
        cmbSurname.setBounds(120, 70, 150, 20);
        loadSurnames();

        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(350, 250, 100, 30);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 15));
        btnSubmit.addActionListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(350, 300, 100, 30);
        btnBack.setFont(new Font("Arial", Font.BOLD, 15));
        btnBack.addActionListener(this);

        add(lblTitle);
        add(lblStatus);
        add(lblReason);
        add(txtfldReason);
        add(cmbAttend);
        add(cmbSurname);
        add(btnSubmit);
        add(btnBack);
        add(lblSname);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadSurnames() {
        String query = "SELECT DISTINCT surname FROM students";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String surname = rs.getString("surname");
                cmbSurname.addItem(surname);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load surnames: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
        } else if (e.getSource() == btnSubmit) {
            submitAttendance();
        }
    }

    private void submitAttendance() {
        String status = cmbAttend.getSelectedItem().toString();
        String reason = txtfldReason.getText();

        String surname = cmbSurname.getSelectedItem().toString();
        String date = JOptionPane.showInputDialog(this, "Enter Date (MM-DD-YYYY):");

        if (surname != null && !surname.isEmpty() && date != null && !date.isEmpty()) {
            // Check if the record already exists
            if (attendanceRecordExists(surname, date)) {
                JOptionPane.showMessageDialog(this, "Attendance record for this student on this date already exists.", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String query = "INSERT INTO attendance (surname, date, status, reason) VALUES (?, ?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, surname);
                pstmt.setString(2, date);
                pstmt.setString(3, status);
                pstmt.setString(4, reason);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Attendance record submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (attendanceDashboard != null) {
                    attendanceDashboard.loadAttendance();
                }
                dispose();  // Close the current window
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to submit attendance record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "All fields must be filled correctly.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean attendanceRecordExists(String surname, String date) {
        String query = "SELECT COUNT(*) FROM attendance WHERE surname = ? AND date = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, surname);
            pstmt.setString(2, date);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Attendance(null).setVisible(true));
    }
}
