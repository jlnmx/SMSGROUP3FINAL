package sms2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Attendance extends JFrame implements ActionListener {
    private JLabel lblStatus, lblTitle, lblSurname, lblFirstname, lblCourse, lblYear, lblDate;
    private JTextField txtfldSurname, txtfldFirstname, txtfldYear;
    private JButton btnSubmit, btnBack, btnView;
    private JComboBox<String> cmbCourse;
    private JRadioButton rbPresent, rbAbsent;
    private ButtonGroup bgStatus;
    private JSpinner spinnerDate;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";
    private AttendanceDashboard attendanceDashboard;

    public Attendance(AttendanceDashboard dashboard) {
        this.attendanceDashboard = dashboard;
        setTitle("Attendance");
        setSize(500, 400);
        setLayout(null);
        
        getContentPane().setBackground(new Color(245, 245, 220));


        lblTitle = new JLabel("Attendance");
        lblTitle.setBounds(150, 30, 200, 20);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(128, 0, 0));
                
        lblSurname = new JLabel("Surname");
        lblSurname.setBounds(40, 70, 100, 20);
        lblSurname.setFont(new Font("Arial", Font.BOLD, 15));
        lblSurname.setForeground(new Color(128, 0, 0));
                
        lblFirstname = new JLabel("First Name");
        lblFirstname.setBounds(40, 100, 100, 20);
        lblFirstname.setFont(new Font("Arial", Font.BOLD, 15));
        lblFirstname.setForeground(new Color(128, 0, 0));
        
        lblCourse = new JLabel("Course");
        lblCourse.setBounds(40, 130, 100, 20);
        lblCourse.setFont(new Font("Arial", Font.BOLD, 15));
        lblCourse.setForeground(new Color(128, 0, 0));
       
        lblYear = new JLabel("Year");
        lblYear.setBounds(40, 160, 100, 20);
        lblYear.setFont(new Font("Arial", Font.BOLD, 15));
        lblYear.setForeground(new Color(128, 0, 0));
        
        lblStatus = new JLabel("Status");
        lblStatus.setBounds(40, 190, 100, 20);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 15));
        lblStatus.setForeground(new Color(128, 0, 0));
                
        lblDate = new JLabel("Date");
        lblDate.setBounds(40, 220, 100, 20);
        lblDate.setFont(new Font("Arial", Font.BOLD, 15));
        lblDate.setForeground(new Color(128, 0, 0));
        
        txtfldSurname = new JTextField();
        txtfldSurname.setBounds(120, 70, 150, 20);
        txtfldSurname.setFont(new Font("Arial", Font.PLAIN, 15));

        txtfldFirstname = new JTextField();
        txtfldFirstname.setBounds(120, 100, 150, 20);
        txtfldFirstname.setFont(new Font("Arial", Font.PLAIN, 15));

        txtfldYear = new JTextField();
        txtfldYear.setBounds(120, 160, 150, 20);
        txtfldYear.setFont(new Font("Arial", Font.PLAIN, 15));

        rbPresent = new JRadioButton("Present");
        rbPresent.setBounds(120, 190, 80, 20);

        rbAbsent = new JRadioButton("Absent");
        rbAbsent.setBounds(200, 190, 80, 20);

        bgStatus = new ButtonGroup();
        bgStatus.add(rbPresent);
        bgStatus.add(rbAbsent);

        cmbCourse = new JComboBox<>(new String[]{"BEED", "BSA", "BSBA-HRM", "BSCpE", "BSED-EN", "BSED-SS", "BSIE", "BSIT", "BSPSY", "DCPET", "DIT"});
        cmbCourse.setBounds(120, 130, 150, 20);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinnerDate = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "yyyy-MM-dd");
        spinnerDate.setEditor(dateEditor);
        spinnerDate.setBounds(120, 220, 150, 20);

        btnView = new JButton("View");
        btnView.setBounds(350, 200, 100, 30);
        btnView.setFont(new Font("Arial", Font.BOLD, 15));
        btnView.setBackground(new Color(128,0,0));
        btnView.setForeground(Color.WHITE);
        btnView.addActionListener(this);

        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(350, 250, 100, 30);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 15));
        btnSubmit.setBackground(new Color(128,0,0));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.addActionListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(350, 300, 100, 30);
        btnBack.setFont(new Font("Arial", Font.BOLD, 15));
        btnBack.setBackground(new Color(128,0,0));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);

        add(lblTitle);
        add(lblSurname);
        add(lblFirstname);
        add(lblCourse);
        add(lblYear);
        add(lblStatus);
        add(lblDate);
        add(txtfldSurname);
        add(txtfldFirstname);
        add(txtfldYear);
        add(rbPresent);
        add(rbAbsent);
        add(cmbCourse);
        add(spinnerDate);
        add(btnSubmit);
        add(btnBack);
        add(btnView);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            new AdminMenu().setVisible(true);
            dispose();
        } else if (e.getSource() == btnSubmit) {
            submitAttendance();
            dispose();
            new AttendanceDashboard().setVisible(true);
        } else if (e.getSource() == btnView) {
            new AttendanceDashboard().setVisible(true);
            dispose();
        }
    }

    private void submitAttendance() {
        String surname = txtfldSurname.getText();
        String firstname = txtfldFirstname.getText();
        String course = cmbCourse.getSelectedItem().toString();
        String year = txtfldYear.getText();
        String status = rbPresent.isSelected() ? "Present" : "Absent";
        Date date = (Date) spinnerDate.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        if (surname != null && !surname.isEmpty() && firstname != null && !firstname.isEmpty() && formattedDate != null && !formattedDate.isEmpty() && course != null && !course.isEmpty() && year != null && !year.isEmpty()) {
            if (attendanceRecordExists(surname, firstname, course, year, formattedDate)) {
                JOptionPane.showMessageDialog(this, "Attendance record for this student on this date already exists.", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String query = "INSERT INTO attendance (surname, firstname, course, year, date, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, surname);
                pstmt.setString(2, firstname);
                pstmt.setString(3, course);
                pstmt.setString(4, year);
                pstmt.setString(5, formattedDate);
                pstmt.setString(6, status);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Attendance record submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (attendanceDashboard != null) {
                    attendanceDashboard.loadAttendance();
                }
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to submit attendance record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "All fields must be filled correctly.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean attendanceRecordExists(String surname, String firstname, String course, String year, String date) {
        String query = "SELECT COUNT(*) FROM attendance WHERE surname = ? AND firstname = ? AND course = ? AND year = ? AND date = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, surname);
            pstmt.setString(2, firstname);
            pstmt.setString(3, course);
            pstmt.setString(4, year);
            pstmt.setString(5, date);
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
