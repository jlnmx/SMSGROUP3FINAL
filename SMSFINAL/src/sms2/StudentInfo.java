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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentInfo extends JFrame implements ActionListener {
    private JLabel lblSurname, lblFirstname, lblMiddlename, lblBday, lblContact, lblEmail, lblStudentnum, lblGender, lblCourse, lblYear, lblTitle;
    private JTextField txtfldSurname, txtfldFirstname, txtfldMiddlename, txtfldContact, txtfldStudentnum, txtfldYear;
    private JRadioButton rbtnMale, rbtnFemale, rbtnOther;
    private ButtonGroup genderGroup;
    private JButton btnSubmit, btnBack, btnClear;
    private JSpinner dateSpinner;
    private JComboBox<String> comboCourse;
    private JTextField txtfldEmail;

    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USERNAME = "maxxi";
    private static final String PASSWORD = "01282004";

    public StudentInfo() {
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("Student Information");
        setLayout(null);
        

        getContentPane().setBackground(new Color(245, 245, 220));

        lblTitle = new JLabel("Student Information");
        lblTitle.setBounds(200, 5, 400, 30);
        lblTitle.setFont(new Font("Bell MT", Font.BOLD, 28));
        lblTitle.setForeground(new Color(128, 0, 0));

        lblSurname = new JLabel("Surname");
        lblSurname.setBounds(75, 50, 150, 25);
        lblSurname.setFont(new Font("Arial", Font.BOLD, 18));

        lblFirstname = new JLabel("First Name");
        lblFirstname.setBounds(75, 90, 150, 25);
        lblFirstname.setFont(new Font("Arial", Font.BOLD, 18));

        lblMiddlename = new JLabel("Middle Name");
        lblMiddlename.setBounds(75, 130, 150, 25);
        lblMiddlename.setFont(new Font("Arial", Font.BOLD, 18));

        lblStudentnum = new JLabel("Student No.");
        lblStudentnum.setBounds(75, 170, 150, 25);
        lblStudentnum.setFont(new Font("Arial", Font.BOLD, 18));

        lblBday = new JLabel("Birthday");
        lblBday.setBounds(75, 210, 150, 25);
        lblBday.setFont(new Font("Arial", Font.BOLD, 18));

        lblGender = new JLabel("Gender");
        lblGender.setBounds(75, 250, 150, 25);
        lblGender.setFont(new Font("Arial", Font.BOLD, 18));

        lblContact = new JLabel("Contact Number");
        lblContact.setBounds(75, 290, 150, 25);
        lblContact.setFont(new Font("Arial", Font.BOLD, 18));

        lblEmail = new JLabel("Email Address");
        lblEmail.setBounds(75, 330, 150, 25);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 18));

        lblCourse = new JLabel("Course");
        lblCourse.setBounds(75, 370, 150, 25);
        lblCourse.setFont(new Font("Arial", Font.BOLD, 18));

        lblYear = new JLabel("Year");
        lblYear.setBounds(75, 410, 150, 25);
        lblYear.setFont(new Font("Arial", Font.BOLD, 18));

        txtfldSurname = new JTextField();
        txtfldSurname.setBounds(250, 50, 225, 25);
        txtfldSurname.setFont(new Font("Arial", Font.PLAIN, 20));

        txtfldFirstname = new JTextField();
        txtfldFirstname.setBounds(250, 90, 225, 25);
        txtfldFirstname.setFont(new Font("Arial", Font.PLAIN, 20));

        txtfldMiddlename = new JTextField();
        txtfldMiddlename.setBounds(250, 130, 225, 25);
        txtfldMiddlename.setFont(new Font("Arial", Font.PLAIN, 20));

        txtfldStudentnum = new JTextField();
        txtfldStudentnum.setBounds(250, 170, 225, 25);
        txtfldStudentnum.setFont(new Font("Arial", Font.PLAIN, 20));

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setBounds(250, 210, 125, 25);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        rbtnMale = new JRadioButton("Male");
        rbtnMale.setBounds(250, 250, 100, 25);
        rbtnMale.setFont(new Font("Arial", Font.PLAIN, 18));

        rbtnFemale = new JRadioButton("Female");
        rbtnFemale.setBounds(350, 250, 100, 25);
        rbtnFemale.setFont(new Font("Arial", Font.PLAIN, 18));

        rbtnOther = new JRadioButton("Other");
        rbtnOther.setBounds(450, 250, 100, 25);
        rbtnOther.setFont(new Font("Arial", Font.PLAIN, 18));

        genderGroup = new ButtonGroup();
        genderGroup.add(rbtnMale);
        genderGroup.add(rbtnFemale);
        genderGroup.add(rbtnOther);

        txtfldContact = new JTextField();
        txtfldContact.setBounds(250, 290, 225, 25);
        txtfldContact.setFont(new Font("Arial", Font.PLAIN, 20));

        txtfldEmail = new JTextField();
        txtfldEmail.setBounds(250, 330, 250, 25);
        txtfldEmail.setFont(new Font("Arial", Font.PLAIN, 20));

        String[] courses = {"BEED", "BSA", "BSBA-HRM", "BSCpE", "BSED-EN", "BSED-SS", "BSIE", "BSIT", "BSPSY", "DCPET", "DIT"};
        comboCourse = new JComboBox<>(courses);
        comboCourse.setBounds(250, 370, 140, 25);
        comboCourse.setFont(new Font("Arial", Font.PLAIN, 18));

        txtfldYear = new JTextField();
        txtfldYear.setBounds(250, 410, 150, 25);
        txtfldYear.setFont(new Font("Arial", Font.PLAIN, 18));

        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(500, 460, 100, 40);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 18));
        btnSubmit.setBackground(new Color(128, 0, 0));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.addActionListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(75, 460, 100, 40);
        btnBack.setFont(new Font("Arial", Font.BOLD, 18));
        btnBack.setBackground(new Color(128, 0, 0));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);

        btnClear = new JButton("Clear");
        btnClear.setBounds(250, 460, 100, 40);
        btnClear.setFont(new Font("Arial", Font.BOLD, 18));
        btnClear.setBackground(new Color(128, 0, 0));
        btnClear.setForeground(Color.WHITE);
        btnClear.addActionListener(this);

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
        add(btnClear);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            try {
                String surname = txtfldSurname.getText().trim();
                String firstname = txtfldFirstname.getText().trim();
                String middlename = txtfldMiddlename.getText().trim();
                String studentnum = txtfldStudentnum.getText().trim();
                String birthday = new SimpleDateFormat("yyyy-MM-dd").format(dateSpinner.getValue());
                String gender = "";
                if (rbtnMale.isSelected()) {
                    gender = "Male";
                } else if (rbtnFemale.isSelected()) {
                    gender = "Female";
                } else {
                    gender = "Other";
                }
                String contactNumber = txtfldContact.getText().trim();
                String emailAddress = txtfldEmail.getText().trim();
                String course = (String) comboCourse.getSelectedItem();
                String year = txtfldYear.getText().trim();

                
                Pattern namePattern = Pattern.compile("^[A-Za-z ]{1,50}$");
                Matcher surnameMatcher = namePattern.matcher(surname);
                Matcher firstnameMatcher = namePattern.matcher(firstname);
                Matcher middlenameMatcher = namePattern.matcher(middlename);

                if (!surnameMatcher.matches()) {
                    JOptionPane.showMessageDialog(this, "Invalid Surname", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!firstnameMatcher.matches()) {
                    JOptionPane.showMessageDialog(this, "Invalid First Name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!middlenameMatcher.matches()) {
                    JOptionPane.showMessageDialog(this, "Invalid Middle Name", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Pattern contactPattern = Pattern.compile("^[0-9]{10,15}$");
                Matcher contactMatcher = contactPattern.matcher(contactNumber);

                if (!contactMatcher.matches()) {
                    JOptionPane.showMessageDialog(this, "Invalid Contact Number", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Pattern emailPattern = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-z]{2,3}$");
                Matcher emailMatcher = emailPattern.matcher(emailAddress);

                if (!emailMatcher.matches()) {
                    JOptionPane.showMessageDialog(this, "Invalid Email Address.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                String query = "INSERT INTO students (surname, firstname, middlename, studentnum, birthday, gender, contactNumber, emailAddress, course, year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, surname);
                ps.setString(2, firstname);
                ps.setString(3, middlename);
                ps.setString(4, studentnum);
                ps.setString(5, birthday);
                ps.setString(6, gender);
                ps.setString(7, contactNumber);
                ps.setString(8, emailAddress);
                ps.setString(9, course);
                ps.setString(10, year);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student information saved successfully.");
                clearFields();

                ps.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving student information: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnBack) {
           new StudentMenu();
            dispose();
        } else if (e.getSource() == btnClear) {
            clearFields();
        }
    }

    private void clearFields() {
        txtfldSurname.setText("");
        txtfldFirstname.setText("");
        txtfldMiddlename.setText("");
        txtfldStudentnum.setText("");
        dateSpinner.setValue(new java.util.Date());
        genderGroup.clearSelection();
        txtfldContact.setText("");
        txtfldEmail.setText("");
        comboCourse.setSelectedIndex(0);
        txtfldYear.setText("");
    }

    public static void main(String[] args) {
        new StudentInfo();
    }
}
