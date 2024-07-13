package sms2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceDashboard extends JFrame implements ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnSave, btnDelete, btnRefresh, btnReturn;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USERNAME = "maxxi";
    private static final String PASSWORD = "01282004";

    public AttendanceDashboard() {
        setTitle("Attendance Dashboard");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        model = new DefaultTableModel(new String[]{"Student Number", "Surname", "First Name", "Course", "Year", "Date", "Status"}, 0);
        table = new JTable(model);

        TableColumn dateColumn = table.getColumnModel().getColumn(5); 
        dateColumn.setCellEditor(new DateEditor());

        TableColumn statusColumn = table.getColumnModel().getColumn(6); 
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Present", "Absent"});
        statusColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        btnSave.setBackground(new Color(128, 0, 0));
        btnSave.setForeground(Color.WHITE);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(this);
        btnDelete.setBackground(new Color(128, 0, 0));
        btnDelete.setForeground(Color.WHITE);

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(this);
        btnRefresh.setBackground(new Color(128, 0, 0));
        btnRefresh.setForeground(Color.WHITE);

        btnReturn = new JButton("Return");
        btnReturn.addActionListener(this);
        btnReturn.setBackground(new Color(128, 0, 0));
        btnReturn.setForeground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.add(btnSave);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        panel.add(btnReturn);

        add(panel, BorderLayout.SOUTH);

        loadAttendanceData();

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadAttendanceData() {
        model.setRowCount(0);

        String query = "SELECT s.studentnum, s.surname, s.firstname, s.course, s.year, a.date, a.status " +
                "FROM students s LEFT JOIN attendance a ON s.studentnum = a.studentnum";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String studentNumber = rs.getString("studentnum");
                String surname = rs.getString("surname");
                String firstName = rs.getString("firstname");
                String course = rs.getString("course");
                String year = rs.getString("year");
                java.sql.Date date = rs.getDate("date");
                String status = rs.getString("status");

                model.addRow(new Object[]{studentNumber, surname, firstName, course, year, date, status});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load attendance data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Object studentNumberObj = model.getValueAt(selectedRow, 0);
                if (studentNumberObj instanceof String) {
                    String studentNumber = (String) studentNumberObj;
                    Object dateObj = model.getValueAt(selectedRow, 5); 
                    String date = (dateObj != null) ? new SimpleDateFormat("yyyy-MM-dd").format((Date) dateObj) : null;
                    String status = (String) model.getValueAt(selectedRow, 6); 

                    if (date != null && status != null) {
                        updateAttendance(studentNumber, date, status);
                    } else {
                        JOptionPane.showMessageDialog(this, "Date and Status cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid student number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnDelete) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentNumber = (String) model.getValueAt(selectedRow, 0); 
                deleteAttendance(studentNumber);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnRefresh) {
            loadAttendanceData();
        } else if (e.getSource() == btnReturn) {
            dispose();
            new AdminMenu().setVisible(true);
        }
    }

    private void updateAttendance(String studentNumber, String date, String status) {
    String selectQuery = "SELECT * FROM attendance WHERE studentnum = ? AND date = ?";
    String insertQuery = "INSERT INTO attendance (studentnum, surname, firstname, course, year, date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    String updateQuery = "UPDATE attendance SET status = ? WHERE studentnum = ? AND date = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
         PreparedStatement pstmtSelect = conn.prepareStatement(selectQuery);
         PreparedStatement pstmtInsert = conn.prepareStatement(insertQuery);
         PreparedStatement pstmtUpdate = conn.prepareStatement(updateQuery)) {

        pstmtSelect.setString(1, studentNumber);
        pstmtSelect.setDate(2, java.sql.Date.valueOf(date));
        ResultSet rs = pstmtSelect.executeQuery();

        if (rs.next()) {
            pstmtUpdate.setString(1, status);
            pstmtUpdate.setString(2, studentNumber);
            pstmtUpdate.setDate(3, java.sql.Date.valueOf(date));
            pstmtUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Attendance updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String query = "SELECT surname, firstname, course, year FROM students WHERE studentnum = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, studentNumber);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String surname = resultSet.getString("surname");
                    String firstName = resultSet.getString("firstname");
                    String course = resultSet.getString("course");
                    String year = resultSet.getString("year");

                    pstmtInsert.setString(1, studentNumber);
                    pstmtInsert.setString(2, surname);
                    pstmtInsert.setString(3, firstName);
                    pstmtInsert.setString(4, course);
                    pstmtInsert.setString(5, year);
                    pstmtInsert.setDate(6, java.sql.Date.valueOf(date));
                    pstmtInsert.setString(7, status);

                    pstmtInsert.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Attendance added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found for student number: " + studentNumber, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        loadAttendanceData();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to update attendance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void deleteAttendance(String studentNumber) {
        String deleteQuery = "DELETE FROM attendance WHERE studentnum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, studentNumber);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Attendance deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAttendanceData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete attendance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceDashboard());
    }

    class DateEditor extends AbstractCellEditor implements TableCellEditor {
        private final JSpinner spinner;

        public DateEditor() {
            spinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
            spinner.setEditor(editor);
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Date) {
                spinner.setValue(value);
            } else {
                spinner.setValue(new Date());
            }
            return spinner;
        }
    }
}
