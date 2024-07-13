package sms2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends JFrame implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnLoadData, btnAddStudent, btnEditStudent, btnDeleteStudent, btnLogout, btnBack;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();

        btnLoadData = new JButton("Load Data");
        btnLoadData.setBackground(new Color(128, 0, 0));
        btnLoadData.setForeground(Color.WHITE);

        btnAddStudent = new JButton("Add Student");
        btnAddStudent.setBackground(new Color(128, 0, 0));
        btnAddStudent.setForeground(Color.WHITE);

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
        btnAddStudent.addActionListener(this);
        btnEditStudent.addActionListener(this);
        btnDeleteStudent.addActionListener(this);
        btnLogout.addActionListener(this);
        btnBack.addActionListener(this);

        panel.add(btnLoadData);
        panel.add(btnAddStudent);
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
        if (e.getSource() == btnAddStudent) {
            SwingUtilities.invokeLater(() -> new StudentInfo());
            dispose();
        } else if (e.getSource() == btnEditStudent) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String studentnum = table.getValueAt(selectedRow, 0).toString();
                editStudent(studentnum);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == btnLoadData) {
            loadData();
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
         PreparedStatement pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
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

            String newSurname = JOptionPane.showInputDialog(this, "Enter Surname:", surname);
            String newFirstname = JOptionPane.showInputDialog(this, "Enter First Name:", firstname);
            String newMiddlename = JOptionPane.showInputDialog(this, "Enter Middle Name:", middlename);
            
            String[] genders = {"Male", "Female"};
            JComboBox<String> genderComboBox = new JComboBox<>(genders);
            genderComboBox.setSelectedItem(gender); 
            
            JPanel genderPanel = new JPanel();
            genderPanel.add(new JLabel("Select Gender:"));
            genderPanel.add(genderComboBox);
            
            int option = JOptionPane.showConfirmDialog(this, genderPanel, "Enter Gender", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String newGender = (String) genderComboBox.getSelectedItem();
                String newContactNumber = JOptionPane.showInputDialog(this, "Enter Contact Number:", contactNumber);
                String newEmailAddress = JOptionPane.showInputDialog(this, "Enter Email Address:", emailAddress);
                String newCourse = JOptionPane.showInputDialog(this, "Enter Course:", course);
                String newYear = JOptionPane.showInputDialog(this, "Enter Year:", year);

                JSpinner spinner = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
                spinner.setEditor(editor);
                if (birthday != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdf.parse(birthday);
                        spinner.setValue(date);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }

                int birthdayOption = JOptionPane.showConfirmDialog(this, spinner, "Select Birthday:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (birthdayOption == JOptionPane.OK_OPTION) {
                    Date newBirthday = (Date) spinner.getValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String newBirthdayStr = sdf.format(newBirthday);

                    rs.updateString("surname", newSurname);
                    rs.updateString("firstname", newFirstname);
                    rs.updateString("middlename", newMiddlename);
                    rs.updateString("gender", newGender);
                    rs.updateString("contactNumber", newContactNumber);
                    rs.updateString("emailAddress", newEmailAddress);
                    rs.updateString("course", newCourse);
                    rs.updateString("year", newYear);
                    rs.updateString("birthday", newBirthdayStr);

                    rs.updateRow();
                    JOptionPane.showMessageDialog(this, "Student updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                    loadData();
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void deleteStudent(String studentnum) {
    String deleteStudentQuery = "DELETE FROM students WHERE studentnum = ?";
    String deleteAttendanceQuery = "DELETE FROM attendance WHERE studentnum = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmtStudent = conn.prepareStatement(deleteStudentQuery);
         PreparedStatement pstmtAttendance = conn.prepareStatement(deleteAttendanceQuery)) {

        conn.setAutoCommit(false);

        pstmtStudent.setString(1, studentnum);
        int rowsAffectedStudents = pstmtStudent.executeUpdate();

        pstmtAttendance.setString(1, studentnum);
        int rowsAffectedAttendance = pstmtAttendance.executeUpdate();

        conn.commit(); 

        if (rowsAffectedStudents > 0) {
            JOptionPane.showMessageDialog(this, "Student record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData(); 
        } else {
            JOptionPane.showMessageDialog(this, "No student found with student number: " + studentnum, "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Failed to delete student and attendance records: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}
