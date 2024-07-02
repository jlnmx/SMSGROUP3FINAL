package sms2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Grades extends JFrame implements ActionListener {

    private JLabel lblSubject, lblSurName, lblFirstName, lblFinal, lblMidterm, lblCourse;

    private JTextField txtfldSurName = new JTextField();
    private JTextField txtfldFirstName = new JTextField();
    private JTextField txtfldSubject = new JTextField();
    private JTextField txtfldMidterm = new JTextField();
    private JTextField txtfldFinal = new JTextField();

    private JComboBox<String> cmbCourse;
    private JButton btnResult;
    private JButton btnClear;
    private JButton btnView;

    private JTextArea txtareaLoginResults = new JTextArea();

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/sms";
    private static final String USER = "maxxi";
    private static final String PASSWORD = "01282004";

    public Grades() {
        setTitle("Student Grades");
        setSize(500, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblSurName = new JLabel("Surname:");
        lblSurName.setBounds(20, 20, 100, 30);
        lblSurName.setFont(new Font("Arial", Font.BOLD, 14));

        lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(20, 60, 100, 30);
        lblFirstName.setFont(new Font("Arial", Font.BOLD, 14));

        lblCourse = new JLabel("Course:");
        lblCourse.setBounds(20, 100, 100, 30);
        lblCourse.setFont(new Font("Arial", Font.BOLD, 14));

        lblSubject = new JLabel("Subject:");
        lblSubject.setBounds(20, 140, 100, 30);
        lblSubject.setFont(new Font("Arial", Font.BOLD, 14));

        lblMidterm = new JLabel("Midterms:");
        lblMidterm.setBounds(20, 180, 100, 30);
        lblMidterm.setFont(new Font("Arial", Font.BOLD, 14));

        lblFinal = new JLabel("Finals:");
        lblFinal.setBounds(20, 220, 100, 30);
        lblFinal.setFont(new Font("Arial", Font.BOLD, 14));

        txtfldSurName.setBounds(140, 20, 150, 30);
        txtfldSurName.setFont(new Font("Arial", Font.PLAIN, 14));

        txtfldFirstName.setBounds(140, 60, 150, 30);
        txtfldFirstName.setFont(new Font("Arial", Font.PLAIN, 14));

        String[] courses = {"BEED", "BSA", "BSBA-HRM", "BSCpE", "BSED-EN", "BSED-SS", "BSIE", "BSIT", "BSPSY", "DCPET", "DIT"};
        cmbCourse = new JComboBox<>(courses);
        cmbCourse.setBounds(140, 100, 150, 30);
        cmbCourse.setFont(new Font("Arial", Font.PLAIN, 14));

        txtfldSubject.setBounds(140, 140, 150, 30);
        txtfldSubject.setFont(new Font("Arial", Font.PLAIN, 14));

        txtfldMidterm.setBounds(140, 180, 150, 30);
        txtfldMidterm.setFont(new Font("Arial", Font.PLAIN, 14));

        txtfldFinal.setBounds(140, 220, 150, 30);
        txtfldFinal.setFont(new Font("Arial", Font.PLAIN, 14));

        btnResult = new JButton("Result");
        btnResult.setBounds(20, 470, 100, 40);
        btnResult.setFont(new Font("Arial", Font.BOLD, 14));
        btnResult.addActionListener(this);
        btnResult.setBackground(Color.GREEN);

        btnClear = new JButton("Clear");
        btnClear.setBounds(140, 470, 100, 40);
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.addActionListener(this);
        btnClear.setBackground(Color.RED);

        btnView = new JButton("View");
        btnView.setBounds(260, 470, 100, 40);
        btnView.setFont(new Font("Arial", Font.BOLD, 14));
        btnView.addActionListener(this);

        txtareaLoginResults.setFont(new Font("Arial", Font.PLAIN, 14));
        txtareaLoginResults.setBounds(20, 270, 450, 180);
        txtareaLoginResults.setEditable(false);

        add(lblSurName);
        add(lblFirstName);
        add(lblSubject);
        add(lblMidterm);
        add(lblFinal);
        add(lblCourse);

        add(txtfldSurName);
        add(txtfldFirstName);
        add(txtfldSubject);
        add(txtfldMidterm);
        add(txtfldFinal);
        add(cmbCourse);

        add(btnResult);
        add(btnClear);
        add(btnView);
        add(txtareaLoginResults);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnView) {
            SwingUtilities.invokeLater(() -> {
                new GradesDashboard();
            });
        } else if (e.getSource() == btnClear) {
            clearFields();
        } else if (e.getSource() == btnResult) {
            calculateResult();
                    }
    }


    private void clearFields() {
        txtfldSurName.setText("");
        txtfldFirstName.setText("");
        txtfldSubject.setText("");
        txtfldMidterm.setText("");
        txtfldFinal.setText("");
        cmbCourse.setSelectedIndex(0);
        txtareaLoginResults.setText("");
    }

    private void calculateResult() {
        try {
            String surname = txtfldSurName.getText();
            String firstname = txtfldFirstName.getText();
            String course = cmbCourse.getSelectedItem().toString();
            String subject = txtfldSubject.getText();

            String midtermText = txtfldMidterm.getText();
            String finalText = txtfldFinal.getText();

            if (midtermText.isEmpty() || finalText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter values for both midterm and final grades.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("Midterm: " + midtermText);
            System.out.println("Final: " + finalText);

            double midterm = Double.parseDouble(midtermText);
            double finals = Double.parseDouble(finalText);

            double average = (midterm + finals) / 2;
            average = Math.round(average * 4) / 4.0;

            String remarks = (average <= 3.0) ? "Passed" : "Failed";
            
             txtareaLoginResults.setText("Surname: " + surname
                    + "\nFirst Name: " + firstname
                    + "\nSubject: " + subject
                    + "\nCourse :" + course
                    + "\n\nMidterm Grade: " + midterm
                    + "\nFinal Grade: " + finals
                    + "\n\nAverage Grade: " + average
                    + "\nRemarks: " + remarks);

            addGradeToTable(surname, firstname, course, subject, midterm, finals, remarks);
            JOptionPane.showMessageDialog(this, "Grade added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for grades.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addGradeToTable(String surname, String firstname, String course, String subject, double midterm, double finals, String remarks) {
        String query = "INSERT INTO grades (surname, first_name, course, subject, midterm, finals, remarks) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, surname);
            pstmt.setString(2, firstname);
            pstmt.setString(3, course);
            pstmt.setString(4, subject);
            pstmt.setDouble(5, midterm);
            pstmt.setDouble(6, finals);
            pstmt.setString(7, remarks);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add grade record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Grades();
    }
}
