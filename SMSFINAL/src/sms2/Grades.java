package sms2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GradesDashboard extends JFrame implements ActionListener {
    private JTable tableGrades;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    private JButton btnAdd, btnDelete, btnReturn;

    public GradesDashboard() {
        setTitle("Grades Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        tableModel = new DefaultTableModel();
        tableGrades = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableGrades);
        add(scrollPane, BorderLayout.CENTER);

        String[] columns = {"Surname", "First Name", "Course", "Subject", "Midterm", "Finals", "Average", "Remarks"};
        tableModel.setColumnIdentifiers(columns);

        JPanel panelButtons = new JPanel(new FlowLayout());
        btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(128, 0, 0)); 
        btnAdd.setForeground(Color.WHITE);
        
        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(128, 0, 0)); 
        btnDelete.setForeground(Color.WHITE);
        
        
        btnReturn = new JButton("Return");
        btnReturn.setBackground(new Color(128, 0, 0)); 
        btnReturn.setForeground(Color.WHITE);

        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnReturn.addActionListener(this);

        panelButtons.add(btnAdd);
        panelButtons.add(btnDelete);
        panelButtons.add(btnReturn);

        add(panelButtons, BorderLayout.SOUTH);

        loadGradesData();

        setVisible(true);
    }

    private void loadGradesData() {
        String query = "SELECT * FROM grades";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String surname = rs.getString("surname");
                String firstname = rs.getString("first_name");
                String course = rs.getString("course");
                String subject = rs.getString("subject");
                double midterm = rs.getDouble("midterm");
                double finals = rs.getDouble("finals");
                double average = rs.getDouble("average");
                String remarks = rs.getString("remarks");

                Object[] rowData = {surname, firstname, course, subject, midterm, finals, average, remarks};
                tableModel.addRow(rowData);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load grade data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            new Grades();
        } else if (e.getSource() == btnDelete) {
            deleteGrade();
        } else if (e.getSource() == btnReturn) {
            new AdminMenu().setVisible(true);
            dispose();
        }
    }

    private void deleteGrade() {
        int selectedRow = tableGrades.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No row selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String surname = tableModel.getValueAt(selectedRow, 0).toString();
        String firstname = tableModel.getValueAt(selectedRow, 1).toString();
        String course = tableModel.getValueAt(selectedRow, 2).toString();
        String subject = tableModel.getValueAt(selectedRow, 3).toString();

        String query = "DELETE FROM grades WHERE surname = ? AND first_name = ? AND course = ? AND subject = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, surname);
            pstmt.setString(2, firstname);
            pstmt.setString(3, course);
            pstmt.setString(4, subject);
            pstmt.executeUpdate();
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Grade record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete grade record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradesDashboard());
    }
}
