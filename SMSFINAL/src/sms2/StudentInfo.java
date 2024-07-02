package sms2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class StudentInfo extends JFrame implements ActionListener {
    private JLabel lblSurname, lblFirstname, lblMiddlename, lblBday, lblContact, lblEmail, lblStudentnum, lblGender, lblCourse, lblYear, lblTitle;
    private JTextField txtfldSurname, txtfldFirstname, txtfldMiddlename, txtfldContact, txtfldEmail, txtfldStudentnum, txtfldYear;
    private JRadioButton rbtnMale, rbtnFemale, rbtnOther;
    private ButtonGroup genderGroup;
    private JButton btnSubmit, btnBack;
    private JSpinner dateSpinner;
    private JComboBox<String> comboCourse;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USERNAME = "maxxi";
    private static final String PASSWORD = "01282004";

    public StudentInfo() {
        setTitle("Student Information");
        setLayout(null);

        lblTitle = new JLabel("Student Information");
        lblTitle.setBounds(200, 5, 200, 20);
        lblTitle.setFont(new Font("Bell MT", Font.BOLD, 20));

        lblSurname = new JLabel("Surname");
        lblSurname.setBounds(75, 40, 100, 20);
        lblSurname.setFont(new Font("Arial", Font.BOLD, 15));

        lblFirstname = new JLabel("First Name");
        lblFirstname.setBounds(75, 70, 100, 20);
        lblFirstname.setFont(new Font("Arial", Font.BOLD, 15));

        lblMiddlename = new JLabel("Middle Name");
        lblMiddlename.setBounds(75, 100, 100, 20);
        lblMiddlename.setFont(new Font("Arial", Font.BOLD, 15));

        lblStudentnum = new JLabel("Student No.");
        lblStudentnum.setBounds(75, 130, 100, 20);
        lblStudentnum.setFont(new Font("Arial", Font.BOLD, 15));

        lblBday = new JLabel("Birthday");
        lblBday.setBounds(75, 160, 100, 20);
        lblBday.setFont(new Font("Arial", Font.BOLD, 15));

        lblGender = new JLabel("Gender");
        lblGender.setBounds(75, 190, 100, 20);
        lblGender.setFont(new Font("Arial", Font.BOLD, 15));

        lblContact = new JLabel("Contact Number");
        lblContact.setBounds(75, 220, 120, 20);
        lblContact.setFont(new Font("Arial", Font.BOLD, 15));

        lblEmail = new JLabel("Email Address");
        lblEmail.setBounds(75, 250, 120, 20);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 15));

        lblCourse = new JLabel("Course");
        lblCourse.setBounds(75, 280, 100, 20);
        lblCourse.setFont(new Font("Arial", Font.BOLD, 15));

        lblYear = new JLabel("Year");
        lblYear.setBounds(75, 310, 100, 20);
        lblYear.setFont(new Font("Arial", Font.BOLD, 15));

        txtfldSurname = new JTextField();
        txtfldSurname.setBounds(200, 40, 200, 20);
        txtfldSurname.setFont(new Font("Arial", Font.PLAIN, 15));

        txtfldFirstname = new JTextField();
        txtfldFirstname.setBounds(200, 70, 200, 20);
        txtfldFirstname.setFont(new Font("Arial", Font.PLAIN, 15));

        txtfldMiddlename = new JTextField();
        txtfldMiddlename.setBounds(200, 100, 200, 20);
        txtfldMiddlename.setFont(new Font("Arial", Font.PLAIN, 15));

        txtfldStudentnum = new JTextField();
        txtfldStudentnum.setBounds(200, 130, 200, 20);
        txtfldStudentnum.setFont(new Font("Arial", Font.PLAIN, 15));

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setBounds(200, 160, 130, 20);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        rbtnMale = new JRadioButton("Male");
        rbtnMale.setBounds(200, 190, 70, 20);
        rbtnMale.setFont(new Font("Arial", Font.PLAIN, 15));

        rbtnFemale = new JRadioButton("Female");
        rbtnFemale.setBounds(270, 190, 80, 20);
        rbtnFemale.setFont(new Font("Arial", Font.PLAIN, 15));

        rbtnOther = new JRadioButton("Other");
        rbtnOther.setBounds(350, 190, 70, 20);
        rbtnOther.setFont(new Font("Arial", Font.PLAIN, 15));

        genderGroup = new ButtonGroup();
        genderGroup.add(rbtnMale);
        genderGroup.add(rbtnFemale);
        genderGroup.add(rbtnOther);

        txtfldContact = new JTextField();
        txtfldContact.setBounds(200, 220, 200, 20);
        txtfldContact.setFont(new Font("Arial", Font.PLAIN, 15));

        txtfldEmail = new JTextField();
        txtfldEmail.setBounds(200, 250, 200, 20);
        txtfldEmail.setFont(new Font("Arial", Font.PLAIN, 15));

        String[] courses = {"BEED", "BSA", "BSBA-HRM", "BSCpE", "BSED-EN", "BSED-SS", "BSIE", "BSIT", "BSPSY", "DCPET", "DIT"};
        comboCourse = new JComboBox<>(courses);
        comboCourse.setBounds(200, 280, 130, 20);
        comboCourse.setFont(new Font("Arial", Font.PLAIN, 15));

        txtfldYear = new JTextField();
        txtfldYear.setBounds(200, 310, 200, 20);
        txtfldYear.setFont(new Font("Arial", Font.PLAIN, 15));

        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(100, 350, 100, 30);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 15));
        btnSubmit.addActionListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(250, 350, 100, 30);
        btnBack.setFont(new Font("Arial", Font.BOLD, 15));
        btnBack.addActionListener(this);

        add(lblTitle);
        add(lblSurname);
        add(lblFirstname);
        add(lblMiddlename);
        add(lblStudentnum);
        add(lblBday);
        add(lblGender);
        add(rbtnMale);
        add(rbtnFemale);
        add(rbtnOther);
        add(lblContact);
        add(lblEmail);
        add(lblCourse);
        add(lblYear);
        add(txtfldSurname);
        add(txtfldFirstname);
        add(txtfldMiddlename);
        add(txtfldStudentnum);
        add(dateSpinner);
        add(txtfldContact);
        add(txtfldEmail);
        add(comboCourse);
        add(txtfldYear);
        add(btnSubmit);
        add(btnBack);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(600, 500);
    }

    public StudentInfo(String surname, String firstname, String middlename, String birthday, String gender, String contactNumber, String emailAddress, String course, String year) {
        this(); 

        txtfldSurname.setText(surname);
        txtfldFirstname.setText(firstname);
        txtfldMiddlename.setText(middlename);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateSpinner.setValue(sdf.parse(birthday));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gender.equalsIgnoreCase("Male")) {
            rbtnMale.setSelected(true);
        } else if (gender.equalsIgnoreCase("Female")) {
            rbtnFemale.setSelected(true);
        } else {
            rbtnOther.setSelected(true);
        }
        txtfldContact.setText(contactNumber);
        txtfldEmail.setText(emailAddress);
        comboCourse.setSelectedItem(course);
        txtfldYear.setText(year);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            saveStudentInfo();
        } else if (e.getSource() == btnBack) {
            dispose(); 
            new StudentMenu();
        }
    }

    private void saveStudentInfo() {
        String studentnum = txtfldStudentnum.getText();
        String surname = txtfldSurname.getText();
        String firstname = txtfldFirstname.getText();
        String middlename = txtfldMiddlename.getText();
        String birthday = new SimpleDateFormat("yyyy-MM-dd").format(dateSpinner.getValue());
        String gender = "";
        if (rbtnMale.isSelected()) {
            gender = "Male";
        } else if (rbtnFemale.isSelected()) {
            gender = "Female";
        } else if (rbtnOther.isSelected()) {
            gender = "Other";
        }
        String contactNumber = txtfldContact.getText();
        String emailAddress = txtfldEmail.getText();
        String course = (String) comboCourse.getSelectedItem();
        String year = txtfldYear.getText();

        String query = "INSERT INTO students (studentnum, surname, firstname, middlename, birthday, gender, contactNumber, emailAddress, course, year) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentnum);
            pstmt.setString(2, surname);
            pstmt.setString(3, firstname);
            pstmt.setString(4, middlename);
            pstmt.setString(5, birthday);
            pstmt.setString(6, gender);
            pstmt.setString(7, contactNumber);
            pstmt.setString(8, emailAddress);
            pstmt.setString(9, course);
            pstmt.setString(10, year);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Student information saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save student information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving student information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentInfo());
    }
}
