package sms2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GradesDashboard extends JFrame implements ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAddGrade, btnDeleteGrade, btnReturn;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USERNAME = "maxxi";
    private static final String PASSWORD = "01282004";

    public GradesDashboard() {
        setTitle("Grades Dashboard");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        model = new DefaultTableModel(new String[]{"Student Number", "Surname", "First Name", "Course", "Subject", "Midterm", "Finals", "Average", "Remarks"}, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        btnAddGrade = new JButton("Add Grade");
        btnAddGrade.addActionListener(this);
        btnAddGrade.setBackground(new Color(128, 0, 0));
        btnAddGrade.setForeground(Color.WHITE);

        btnDeleteGrade = new JButton("Delete Grade");
        btnDeleteGrade.addActionListener(this);
        btnDeleteGrade.setBackground(new Color(128, 0, 0));
        btnDeleteGrade.setForeground(Color.WHITE);

        btnReturn = new JButton("Return");
        btnReturn.addActionListener(this);
        btnReturn.setBackground(new Color(128, 0, 0));
        btnReturn.setForeground(Color.WHITE);

        panelButtons.add(btnAddGrade);
        panelButtons.add(btnDeleteGrade);
        panelButtons.add(btnReturn);

        add(panelButtons, BorderLayout.SOUTH);

        loadGradesData();

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadGradesData() {
        model.setRowCount(0);

        String query = "SELECT s.studentnum, s.surname, s.firstname, s.course, g.subject, g.midterm, g.finals, g.average, g.remarks " +
                "FROM students s LEFT JOIN grades g ON s.studentnum = g.studentnum";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String studentNumber = rs.getString("studentnum");
                String surname = rs.getString("surname");
                String firstName = rs.getString("firstname");
                String course = rs.getString("course");
                String subject = rs.getString("subject");
                Double midterm = rs.getDouble("midterm");
                if (rs.wasNull()) midterm = null;
                Double finals = rs.getDouble("finals");
                if (rs.wasNull()) finals = null;
                Double average = rs.getDouble("average");
                if (rs.wasNull()) average = null;
                String remarks = rs.getString("remarks");
                if (remarks == null) remarks = "";

                if (midterm == null || finals == null) {
                    remarks = "Incomplete";
                }

                model.addRow(new Object[]{studentNumber, surname, firstName, course, subject, midterm, finals, average, remarks});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load grades data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double roundGrade(double grade) {
        double[] possibleGrades = {1.00, 1.25, 1.50, 1.75, 2.00, 2.25, 2.50, 2.75, 3.00, 4.00, 5.00};
        double closestGrade = possibleGrades[0];
        double minDiff = Math.abs(grade - closestGrade);

        for (double possibleGrade : possibleGrades) {
            double diff = Math.abs(grade - possibleGrade);
            if (diff < minDiff) {
                minDiff = diff;
                closestGrade = possibleGrade;
            }
        }

        return closestGrade;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddGrade) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentNumber = (String) model.getValueAt(selectedRow, 0);
                String surname = (String) model.getValueAt(selectedRow, 1);
                String firstName = (String) model.getValueAt(selectedRow, 2);
                String course = (String) model.getValueAt(selectedRow, 3);

                String subject = JOptionPane.showInputDialog(this, "Enter Subject:");
                if (subject != null && !subject.trim().isEmpty()) {
                    String midtermStr = JOptionPane.showInputDialog(this, "Enter Midterm Grade:");
                    String finalsStr = JOptionPane.showInputDialog(this, "Enter Finals Grade:");

                    Double midterm = null;
                    Double finals = null;

                    try {
                        if (midtermStr != null && !midtermStr.trim().isEmpty()) {
                            midterm = Double.parseDouble(midtermStr);
                        }
                        if (finalsStr != null && !finalsStr.trim().isEmpty()) {
                            finals = Double.parseDouble(finalsStr);
                        }

                        Double average = null;
                        String remarks = "Incomplete";

                        if (midterm != null && finals != null) {
                            average = (midterm + finals) / 2;
                            average = roundGrade(average);
                            remarks = average <= 3.00 ? "Passed" : "Failed";
                        }

                        model.addRow(new Object[]{studentNumber, surname, firstName, course, subject, midterm, finals, average, remarks});
                        insertGrades(studentNumber, surname, firstName, subject, midterm, finals, average, remarks);
                        loadGradesData();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Subject cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to add grades.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnDeleteGrade) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentNumber = (String) model.getValueAt(selectedRow, 0);
                String subject = (String) model.getValueAt(selectedRow, 4);

                deleteGrades(studentNumber, subject);
                loadGradesData();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a grade to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnReturn) {
            dispose();
            new AdminMenu().setVisible(true);
        }
    }

    private void insertGrades(String studentNumber, String surname, String firstName, String subject, Double midterm, Double finals, Double average, String remarks) {
        String insertQuery = "INSERT INTO grades (studentnum, surname, firstname, subject, midterm, finals, average, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, studentNumber);
            pstmt.setString(2, surname);
            pstmt.setString(3, firstName);
            pstmt.setString(4, subject);
            if (midterm != null) {
                pstmt.setDouble(5, midterm);
            } else {
                pstmt.setNull(5, Types.DOUBLE);
            }
            if (finals != null) {
                pstmt.setDouble(6, finals);
            } else {
                pstmt.setNull(6, Types.DOUBLE);
            }
            if (average != null) {
                pstmt.setDouble(7, average);
            } else {
                pstmt.setNull(7, Types.DOUBLE);
            }

            if (midterm == null || finals == null) {
                remarks = "Incomplete";
            }

            pstmt.setString(8, remarks);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Grades added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add grades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteGrades(String studentNumber, String subject) {
        String deleteQuery = "DELETE FROM grades WHERE studentnum = ? AND subject = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, studentNumber);
            pstmt.setString(2, subject);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Grades deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete grades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradesDashboard());
    }
}
