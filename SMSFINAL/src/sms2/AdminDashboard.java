package sms2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class AdminDashboard extends JFrame implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnLoadData, btnEditStudent, btnDeleteStudent, btnLogout, btnBack;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        btnLoadData = new JButton("Load Data");
        btnLoadData.setBackground(new Color(128, 0, 0)); 
        btnLoadData.setForeground(Color.WHITE);
                
        btnEditStudent = new JButton("Edit Student");
        btnEditStudent.setBackground(new Color(128, 0, 0)); 
        btnEditStudent.setForeground(Color.WHITE);
                
        btnDeleteStudent = new JButton("Delete Student");
        btnDeleteStudent.setBackground(new Color(128, 0, 0)); 
        btnDeleteStudent.setForeground(Color.WHITE);
        
        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(128, 0, 0)); 
        btnLogout.setForeground(Color.WHITE);
                
        btnBack = new JButton("Back");
        btnBack.setBackground(new Color(128, 0, 0)); 
        btnBack.setForeground(Color.WHITE);

        btnLoadData.addActionListener(this);
        btnEditStudent.addActionListener(this);
        btnDeleteStudent.addActionListener(this);
        btnLogout.addActionListener(this);
        btnBack.addActionListener(this);
        
        panel.add(btnLoadData);
        panel.add(btnEditStudent);
        panel.add(btnDeleteStudent);
        panel.add(btnLogout);
        panel.add(btnBack);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
        loadData();
    }

    private void loadData() {
        String query = "SELECT studentnum AS 'Student Number', surname AS 'Surname', firstname AS 'First Name', " +
                "middlename AS 'Middle Name', birthday AS 'Birthday', gender AS 'Gender', contactNumber AS 'Contact Number', " +
                "emailAddress AS 'Email Address', course AS 'Course', year AS 'Year' FROM students";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);
            }
            tableModel.setColumnIdentifiers(columnNames);

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(130); 
            columnModel.getColumn(1).setPreferredWidth(120);
            columnModel.getColumn(2).setPreferredWidth(120); 
            columnModel.getColumn(3).setPreferredWidth(120); 
            columnModel.getColumn(4).setPreferredWidth(100); 
            columnModel.getColumn(5).setPreferredWidth(80);  
            columnModel.getColumn(6).setPreferredWidth(140);
            columnModel.getColumn(7).setPreferredWidth(180); 
            columnModel.getColumn(8).setPreferredWidth(100); 
            columnModel.getColumn(9).setPreferredWidth(60);  

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLoadData) {
            loadData();
        } else if (e.getSource() == btnEditStudent) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String studentnum = table.getValueAt(selectedRow, 0).toString();
                editStudent(studentnum);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == btnDeleteStudent) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String studentnum = table.getValueAt(selectedRow, 0).toString();
                deleteStudent(studentnum);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == btnLogout) {
            dispose();
            new MainMenu().setVisible(true);
        } else if (e.getSource() == btnBack) {
            dispose();
            new AdminMenu().setVisible(true);
        }
    }

    private void editStudent(String studentnum) {
        String query = "SELECT * FROM students WHERE studentnum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentnum);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String surname = rs.getString("surname");
                String firstname = rs.getString("firstname");
                String middlename = rs.getString("middlename");
                String birthday = rs.getString("birthday");
                String gender = rs.getString("gender");
                String contactNumber = rs.getString("contactNumber");
                String emailAddress = rs.getString("emailAddress");
                String course = rs.getString("course");
                String year = rs.getString("year");

                StudentInfo studentInfo = new StudentInfo(surname, firstname, middlename, birthday, gender, contactNumber, emailAddress, course, year);
                studentInfo.setVisible(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent(String studentnum) {
        String query = "DELETE FROM students WHERE studentnum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentnum);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}
