package sms2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentDashboard extends JFrame implements ActionListener {
    private JTable tableStudent;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    private JButton btnReturn;

    public StudentDashboard() {
        setTitle("Student Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableStudent = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableStudent);
        add(scrollPane, BorderLayout.CENTER);

        String[] columns = {"Surname", "First Name", "Course", "Year", "Subject", "Midterm", "Finals", "Average"};
        tableModel.setColumnIdentifiers(columns);

        JPanel panelButtons = new JPanel();
        btnReturn = new JButton("Return");

        btnReturn.addActionListener(this); 

        panelButtons.add(btnReturn);
        add(panelButtons, BorderLayout.SOUTH);

        loadStudentDataFromDatabase();

        setVisible(true);
    }

    private void loadStudentDataFromDatabase() {
        tableModel.setRowCount(0); 
        String query = "SELECT s.surname, s.firstname, s.course, s.year, g.subject, g.midterm, g.finals, g.average " +
                       "FROM students s " +
                       "LEFT JOIN grades g ON s.surname = g.surname AND s.course = g.course";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String surname = rs.getString("surname");
                String firstName = rs.getString("firstname");
                String course = rs.getString("course");
                String year = rs.getString("year");
                String subject = rs.getString("subject");
                double midterm = rs.getDouble("midterm");
                double finals = rs.getDouble("finals");
                double average = rs.getDouble("average");

                Object[] rowData = {surname, firstName, course, year, subject, midterm, finals, average};
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnReturn) {
            dispose(); 
            new StudentMenu().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard());
    }
}
