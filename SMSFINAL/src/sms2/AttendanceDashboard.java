package sms2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AttendanceDashboard extends JFrame implements ActionListener {
    private JTable tableAttendance;
    private JButton btnLoadAttendance, btnAddAttendance, btnUpdateAttendance, btnDeleteAttendance, btnReturn;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public AttendanceDashboard() {
        setTitle("Attendance Management");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(245, 245, 220));


        tableModel = new DefaultTableModel();
        tableAttendance = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tableAttendance);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new FlowLayout());
        btnLoadAttendance = new JButton("Load Attendance");
        btnLoadAttendance.setBackground(new Color(128, 0, 0));
        btnLoadAttendance.setForeground(Color.WHITE);
        
        btnAddAttendance = new JButton("Add Attendance");
        btnAddAttendance.setBackground(new Color(128,0,0));
        btnAddAttendance.setForeground(Color.WHITE);
        
        btnUpdateAttendance = new JButton("Update Attendance");
        btnUpdateAttendance.setBackground(new Color(128,0,0));
        btnUpdateAttendance.setForeground(Color.WHITE);
        
        btnDeleteAttendance = new JButton("Delete Attendance");
        btnDeleteAttendance.setBackground(new Color(128,0, 0));
        btnDeleteAttendance.setForeground(Color.WHITE);

        btnReturn = new JButton("Return");
        btnReturn.setBackground(new Color(128,0,0));
        btnReturn.setForeground(Color.WHITE);

        btnLoadAttendance.addActionListener(this);
        btnAddAttendance.addActionListener(this);
        btnUpdateAttendance.addActionListener(this);
        btnDeleteAttendance.addActionListener(this);
        btnReturn.addActionListener(this);

        panelButtons.add(btnLoadAttendance);
        panelButtons.add(btnAddAttendance);
        panelButtons.add(btnUpdateAttendance);
        panelButtons.add(btnDeleteAttendance);
        panelButtons.add(btnReturn);
        add(panelButtons, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLoadAttendance) {
            loadAttendance();
        } else if (e.getSource() == btnAddAttendance) {
            new Attendance(this).setVisible(true);
        } else if (e.getSource() == btnUpdateAttendance) {
            updateAttendance();
        } else if (e.getSource() == btnDeleteAttendance) {
            deleteAttendance();
        } else if (e.getSource() == btnReturn) {
            new Attendance(this).setVisible(true);
            dispose();
        }
    }

    public void loadAttendance() {
        String query = "SELECT surname, firstname, course, year, date, status FROM attendance";

        try (Connection conn = getConnection();
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
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load attendance records: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAttendance() {
        int selectedRow = tableAttendance.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to update.", "No Record Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String surname = tableModel.getValueAt(selectedRow, 0).toString();
        String firstname = tableModel.getValueAt(selectedRow, 1).toString();
        String course = tableModel.getValueAt(selectedRow, 2).toString();
        String year = tableModel.getValueAt(selectedRow, 3).toString();
        String date = tableModel.getValueAt(selectedRow, 4).toString();
        String status = tableModel.getValueAt(selectedRow, 5).toString();

        JTextField txtStatus = new JTextField(status);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Status:"));
        panel.add(txtStatus);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Attendance", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            status = txtStatus.getText();

            String query = "UPDATE attendance SET status = ? WHERE surname = ? AND firstname = ? AND date = ? AND course = ? AND year = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, status);
                pstmt.setString(2, surname);
                pstmt.setString(3, firstname);
                pstmt.setString(4, date);
                pstmt.setString(5, course);
                pstmt.setString(6, year);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Attendance record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAttendance();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update attendance record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAttendance() {
        int selectedRow = tableAttendance.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "No Record Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String surname = tableModel.getValueAt(selectedRow, 0).toString();
        String firstname = tableModel.getValueAt(selectedRow, 1).toString();
        String date = tableModel.getValueAt(selectedRow, 4).toString();

        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this attendance record?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM attendance WHERE surname = ? AND firstname = ? AND date = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, surname);
                pstmt.setString(2, firstname);
                pstmt.setString(3, date);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Attendance record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAttendance();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete attendance record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceDashboard().setVisible(true));
    }
}
