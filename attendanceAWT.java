package awt_project;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class attendanceAWT extends Frame implements ActionListener {
    // Database connection
    private Connection conn;
    
    // Main panels
    private Panel loginPanel, teacherPanel, studentPanel;
    
    // Teacher sub-panels
    private Panel markAttendancePanel, viewAttendancePanel, monthlySummaryPanel;
    
    // Login components
    private TextField txtUser, txtPass;
    
    // Teacher components
    private Button btnMarkAttendance, btnViewAttendance, btnMonthlySummary, btnLogoutTeacher;
    
    // Mark Attendance components
    private Choice streamBox, classBox, semesterBox, subjectBox;
    private Button loadBtn, submitBtn, backBtn;
    private ArrayList<String> rollNumbers = new ArrayList<>();
    private Checkbox[] studentChecks;
    private Panel studentListHolder;
    
    // View Attendance components
    private Choice streamVA, classVA, semVA, subjectVA;
    private TextField dateField;
    private TextArea resultArea;
    private Button btnLoadAttendance, btnBackVA;
    
    // Monthly Summary components
    private Choice streamMS, classMS, semMS, subjectMS;
    private Button btnLoadSummary, btnBackMS;
    private TextArea summaryArea;
    
    // Student components
    private TextField txtRollNo;
    private Button btnViewToday, btnViewStudentSummary, btnLogoutStudent;

    public attendanceAWT() {
        setTitle("Student-Teacher Attendance System");
        setSize(600, 600);
        setLayout(null);
        setBackground(Color.decode("#f0f8ff"));
        setResizable(false);

        connectDB();
        createLoginPanel();
        createTeacherPanel();
        createStudentPanel();
        createMarkAttendancePanel();
        createViewAttendancePanel();
        createMonthlySummaryPanel();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_system", "root", "sql_24");
        } catch (Exception e) {
            System.out.println("DB Error: " + e);
        }
    }

    private void createLoginPanel() {
        loginPanel = new Panel();
        loginPanel.setLayout(null);
        loginPanel.setBounds(0, 0, 600, 600);

        Label lblLogin = new Label("Login");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 22));
        lblLogin.setBounds(250, 50, 100, 30);
        loginPanel.add(lblLogin);

        Label lblUser = new Label("User ID:");
        lblUser.setBounds(150, 110, 100, 25);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(lblUser);

        txtUser = new TextField();
        txtUser.setBounds(260, 110, 150, 25);
        loginPanel.add(txtUser);

        Label lblPass = new Label("Password:");
        lblPass.setBounds(150, 150, 100, 25);
        lblPass.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(lblPass);

        txtPass = new TextField();
        txtPass.setBounds(260, 150, 150, 25);
        txtPass.setEchoChar('*');
        loginPanel.add(txtPass);

        Button btnLogin = new Button("Login");
        btnLogin.setBounds(230, 200, 100, 30);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(Color.decode("#87ceeb"));
        btnLogin.addActionListener(this);
        loginPanel.add(btnLogin);

        add(loginPanel);
    }

    private void createTeacherPanel() {
        teacherPanel = new Panel();
        teacherPanel.setLayout(null);
        teacherPanel.setBounds(0, 0, 600, 600);
        teacherPanel.setVisible(false);

        Label lblTeacherMenu = new Label("Teacher Menu");
        lblTeacherMenu.setFont(new Font("Arial", Font.BOLD, 20));
        lblTeacherMenu.setBounds(200, 50, 200, 30);
        teacherPanel.add(lblTeacherMenu);

        btnMarkAttendance = new Button("Mark Attendance");
        btnMarkAttendance.setBounds(200, 100, 200, 30);
        btnMarkAttendance.addActionListener(this);
        teacherPanel.add(btnMarkAttendance);

        btnViewAttendance = new Button("View Attendance");
        btnViewAttendance.setBounds(200, 150, 200, 30);
        btnViewAttendance.addActionListener(this);
        teacherPanel.add(btnViewAttendance);

        btnMonthlySummary = new Button("Monthly Summary");
        btnMonthlySummary.setBounds(200, 200, 200, 30);
        btnMonthlySummary.addActionListener(this);
        teacherPanel.add(btnMonthlySummary);

        btnLogoutTeacher = new Button("Logout");
        btnLogoutTeacher.setBounds(230, 260, 140, 30);
        btnLogoutTeacher.setBackground(Color.PINK);
        btnLogoutTeacher.addActionListener(this);
        teacherPanel.add(btnLogoutTeacher);

        add(teacherPanel);
    }

    private void createStudentPanel() {
        studentPanel = new Panel();
        studentPanel.setLayout(null);
        studentPanel.setBounds(0, 0, 600, 600);
        studentPanel.setVisible(false);

        Label lblStudentMenu = new Label("Student Menu");
        lblStudentMenu.setFont(new Font("Arial", Font.BOLD, 20));
        lblStudentMenu.setBounds(200, 50, 200, 30);
        studentPanel.add(lblStudentMenu);

        Label lblRollNo = new Label("Enter Roll Number:");
        lblRollNo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblRollNo.setBounds(150, 120, 150, 30);
        studentPanel.add(lblRollNo);

        txtRollNo = new TextField();
        txtRollNo.setBounds(310, 120, 120, 25);
        studentPanel.add(txtRollNo);

        btnViewToday = new Button("View Today's Attendance");
        btnViewToday.setBounds(180, 180, 220, 30);
        btnViewToday.setFont(new Font("Arial", Font.PLAIN, 14));
        btnViewToday.addActionListener(this);
        studentPanel.add(btnViewToday);

        btnViewStudentSummary = new Button("View Monthly Summary");
        btnViewStudentSummary.setBounds(180, 230, 220, 30);
        btnViewStudentSummary.setFont(new Font("Arial", Font.PLAIN, 14));
        btnViewStudentSummary.addActionListener(this);
        studentPanel.add(btnViewStudentSummary);

        btnLogoutStudent = new Button("Logout");
        btnLogoutStudent.setBounds(230, 290, 140, 30);
        btnLogoutStudent.setBackground(Color.PINK);
        btnLogoutStudent.addActionListener(this);
        studentPanel.add(btnLogoutStudent);

        add(studentPanel);
    }

    private void createMarkAttendancePanel() {
        markAttendancePanel = new Panel();
        markAttendancePanel.setLayout(null);
        markAttendancePanel.setBounds(0, 0, 600, 600);
        markAttendancePanel.setVisible(false);

        streamBox = new Choice();
        streamBox.setBounds(50, 60, 100, 25);
        streamBox.add("BSc IT");
        markAttendancePanel.add(streamBox);

        classBox = new Choice();
        classBox.setBounds(170, 60, 100, 25);
        classBox.add("TY");
        markAttendancePanel.add(classBox);

        semesterBox = new Choice();
        semesterBox.setBounds(290, 60, 100, 25);
        semesterBox.add("5");
        markAttendancePanel.add(semesterBox);

        subjectBox = new Choice();
        subjectBox.setBounds(410, 60, 120, 25);
        subjectBox.add("Java");
        subjectBox.add("Python");
        subjectBox.add("DBMS");
        markAttendancePanel.add(subjectBox);

        streamBox.addItemListener(e -> clearStudentList());
        classBox.addItemListener(e -> clearStudentList());
        semesterBox.addItemListener(e -> clearStudentList());
        subjectBox.addItemListener(e -> clearStudentList());

        loadBtn = new Button("Load Students");
        loadBtn.setBounds(200, 100, 200, 30);
        loadBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loadBtn.addActionListener(this);
        markAttendancePanel.add(loadBtn);

        Panel studentListScrollPanel = new Panel();
        studentListScrollPanel.setLayout(new BorderLayout());
        studentListScrollPanel.setBounds(60, 150, 480, 250);

        studentListHolder = new Panel();
        studentListHolder.setLayout(new GridLayout(0, 1));
        studentListHolder.setSize(500, 500);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(studentListHolder);
        studentListScrollPanel.add(scrollPane);
        markAttendancePanel.add(studentListScrollPanel);

        submitBtn = new Button("Submit Attendance");
        submitBtn.setBounds(200, 420, 200, 30);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(this);
        markAttendancePanel.add(submitBtn);

        backBtn = new Button("Back");
        backBtn.setBounds(20, 500, 80, 30);
        backBtn.setBackground(Color.LIGHT_GRAY);
        backBtn.addActionListener(this);
        markAttendancePanel.add(backBtn);

        add(markAttendancePanel);
    }

    private void createViewAttendancePanel() {
        viewAttendancePanel = new Panel();
        viewAttendancePanel.setLayout(null);
        viewAttendancePanel.setBounds(0, 0, 600, 600);
        viewAttendancePanel.setVisible(false);

        streamVA = new Choice();
        streamVA.setBounds(30, 50, 100, 25);
        streamVA.add("BSc IT");
        streamVA.add("BCA");
        viewAttendancePanel.add(streamVA);

        classVA = new Choice();
        classVA.setBounds(140, 50, 100, 25);
        classVA.add("A");
        classVA.add("B");
        classVA.add("TY");
        viewAttendancePanel.add(classVA);

        semVA = new Choice();
        semVA.setBounds(250, 50, 100, 25);
        semVA.add("1");
        semVA.add("2");
        semVA.add("3");
        semVA.add("5");
        viewAttendancePanel.add(semVA);

        subjectVA = new Choice();
        subjectVA.setBounds(360, 50, 100, 25);
        subjectVA.add("Java");
        subjectVA.add("Python");
        subjectVA.add("DBMS");
        viewAttendancePanel.add(subjectVA);

        dateField = new TextField(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        dateField.setBounds(470, 50, 100, 25);
        viewAttendancePanel.add(dateField);

        btnLoadAttendance = new Button("Load");
        btnLoadAttendance.setBounds(240, 90, 100, 30);
        btnLoadAttendance.addActionListener(this);
        viewAttendancePanel.add(btnLoadAttendance);

        resultArea = new TextArea();
        resultArea.setBounds(60, 140, 480, 300);
        viewAttendancePanel.add(resultArea);

        btnBackVA = new Button("Back");
        btnBackVA.setBounds(20, 500, 80, 30);
        btnBackVA.setBackground(Color.LIGHT_GRAY);
        btnBackVA.addActionListener(this);
        viewAttendancePanel.add(btnBackVA);

        add(viewAttendancePanel);
    }

    private void createMonthlySummaryPanel() {
        monthlySummaryPanel = new Panel();
        monthlySummaryPanel.setLayout(null);
        monthlySummaryPanel.setBounds(0, 0, 600, 600);
        monthlySummaryPanel.setVisible(false);

        streamMS = new Choice();
        streamMS.setBounds(30, 50, 100, 25);
        streamMS.add("BSc IT");
        streamMS.add("BCA");
        monthlySummaryPanel.add(streamMS);

        classMS = new Choice();
        classMS.setBounds(140, 50, 100, 25);
        classMS.add("A");
        classMS.add("B");
        classMS.add("TY");
        monthlySummaryPanel.add(classMS);

        semMS = new Choice();
        semMS.setBounds(250, 50, 100, 25);
        semMS.add("1");
        semMS.add("2");
        semMS.add("3");
        semMS.add("5");
        monthlySummaryPanel.add(semMS);

        subjectMS = new Choice();
        subjectMS.setBounds(360, 50, 120, 25);
        subjectMS.add("Java");
        subjectMS.add("Python");
        subjectMS.add("DBMS");
        monthlySummaryPanel.add(subjectMS);

        btnLoadSummary = new Button("View Summary");
        btnLoadSummary.setBounds(230, 100, 140, 30);
        btnLoadSummary.addActionListener(this);
        monthlySummaryPanel.add(btnLoadSummary);

        summaryArea = new TextArea();
        summaryArea.setBounds(60, 150, 480, 350);
        monthlySummaryPanel.add(summaryArea);

        btnBackMS = new Button("Back");
        btnBackMS.setBounds(20, 520, 80, 30);
        btnBackMS.setBackground(Color.LIGHT_GRAY);
        btnBackMS.addActionListener(this);
        monthlySummaryPanel.add(btnBackMS);

        add(monthlySummaryPanel);
    }

    public void actionPerformed(ActionEvent e) {
        // Handle login
        if (e.getSource() instanceof Button) {
            Button source = (Button) e.getSource();
            
            if (source.getLabel().equals("Login")) {
                handleLogin();
            }
            else if (source == btnMarkAttendance) {
                teacherPanel.setVisible(false);
                markAttendancePanel.setVisible(true);
            }
            else if (source == backBtn) {
                markAttendancePanel.setVisible(false);
                teacherPanel.setVisible(true);
            }
            else if (source == btnLogoutTeacher || source == btnLogoutStudent) {
                handleLogout();
            }
            else if (source == btnViewAttendance) {
                teacherPanel.setVisible(false);
                viewAttendancePanel.setVisible(true);
            }
            else if (source == btnBackVA) {
                viewAttendancePanel.setVisible(false);
                teacherPanel.setVisible(true);
            }
            else if (source == btnMonthlySummary) {
                teacherPanel.setVisible(false);
                monthlySummaryPanel.setVisible(true);
            }
            else if (source == btnBackMS) {
                monthlySummaryPanel.setVisible(false);
                teacherPanel.setVisible(true);
            }
            else if (source == btnLoadAttendance) {
                loadAttendanceRecords();
            }
            else if (source == btnLoadSummary) {
                loadMonthlySummary();
            }
            else if (source == loadBtn) {
                loadStudents();
            }
            else if (source == submitBtn) {
                submitAttendance();
            }
            else if (source == btnViewToday) {
                viewTodaysAttendance();
            }
            else if (source == btnViewStudentSummary) {
                viewStudentMonthlySummary();
            }
        }
    }

    private void handleLogin() {
        String user = txtUser.getText();
        String pass = txtPass.getText();

        try {
            // Check teacher credentials
            PreparedStatement teacherStmt = conn.prepareStatement(
                "SELECT * FROM teachers WHERE username = ? AND password = ?");
            teacherStmt.setString(1, user);
            teacherStmt.setString(2, pass);
            ResultSet teacherRs = teacherStmt.executeQuery();

            if (teacherRs.next()) {
                loginPanel.setVisible(false);
                teacherPanel.setVisible(true);
                return;
            }

            // Check student credentials
            PreparedStatement studentStmt = conn.prepareStatement(
                "SELECT * FROM students WHERE roll_no = ? AND password = ?");
            studentStmt.setString(1, user);
            studentStmt.setString(2, pass);
            ResultSet studentRs = studentStmt.executeQuery();

            if (studentRs.next()) {
                loginPanel.setVisible(false);
                studentPanel.setVisible(true);
                txtRollNo.setText(user); // Auto-fill roll number field
                return;
            }

            showError("Invalid credentials!");
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    private void handleLogout() {
        txtUser.setText("");
        txtPass.setText("");
        txtRollNo.setText("");
        loginPanel.setVisible(true);
        teacherPanel.setVisible(false);
        studentPanel.setVisible(false);
        markAttendancePanel.setVisible(false);
        viewAttendancePanel.setVisible(false);
        monthlySummaryPanel.setVisible(false);
    }

    private void loadStudents() {
        clearStudentList();
        rollNumbers.clear();

        String stream = streamBox.getSelectedItem();
        String className = classBox.getSelectedItem();
        String semester = semesterBox.getSelectedItem();

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT roll_no, name FROM students WHERE stream=? AND class=? AND semester=?");
            ps.setString(1, stream);
            ps.setString(2, className);
            ps.setString(3, semester);
            ResultSet rs = ps.executeQuery();

            ArrayList<Checkbox> checkList = new ArrayList<>();
            while (rs.next()) {
                String roll = rs.getString("roll_no");
                String name = rs.getString("name");
                rollNumbers.add(roll);
                Checkbox cb = new Checkbox(roll + " - " + name);
                cb.setFont(new Font("Arial", Font.PLAIN, 16));
                checkList.add(cb);
                studentListHolder.add(cb);
            }

            studentChecks = checkList.toArray(new Checkbox[0]);
            studentListHolder.revalidate();
            studentListHolder.repaint();
        } catch (Exception ex) {
            showError("Error loading students: " + ex.getMessage());
        }
    }

    private void submitAttendance() {
        String stream = streamBox.getSelectedItem();
        String className = classBox.getSelectedItem();
        String semester = semesterBox.getSelectedItem();
        String subject = subjectBox.getSelectedItem();
        String teacherId = "T001"; // Should be dynamic in real application

        java.util.Date now = new java.util.Date();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(now);
        String time = new SimpleDateFormat("HH:mm:ss").format(now);

        try {
            // Check if attendance already marked for this class/subject/date
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT 1 FROM attendance WHERE stream=? AND class=? AND semester=? AND subject=? AND date=? LIMIT 1");
            checkStmt.setString(1, stream);
            checkStmt.setString(2, className);
            checkStmt.setString(3, semester);
            checkStmt.setString(4, subject);
            checkStmt.setString(5, date);
            
            if (checkStmt.executeQuery().next()) {
                showError("Attendance already marked for this class/subject today!");
                return;
            }

            // Insert attendance records
            for (int i = 0; i < rollNumbers.size(); i++) {
                String roll = rollNumbers.get(i);
                String status = studentChecks[i].getState() ? "P" : "A";

                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO attendance (roll_no, subject, date, time, status, teacher_id, stream, class, semester) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, roll);
                ps.setString(2, subject);
                ps.setString(3, date);
                ps.setString(4, time);
                ps.setString(5, status);
                ps.setString(6, teacherId);
                ps.setString(7, stream);
                ps.setString(8, className);
                ps.setString(9, semester);

                ps.executeUpdate();
            }
            showDialog("Attendance Saved Successfully!");
        } catch (Exception ex) {
            showError("Error saving attendance: " + ex.getMessage());
        }
    }

    private void loadAttendanceRecords() {
        String stream = streamVA.getSelectedItem();
        String cls = classVA.getSelectedItem();
        String sem = semVA.getSelectedItem();
        String subject = subjectVA.getSelectedItem();
        String date = dateField.getText();

        resultArea.setText(""); // clear

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT a.roll_no, s.name, a.status " +
                "FROM attendance a JOIN students s ON a.roll_no = s.roll_no " +
                "WHERE s.stream=? AND s.class=? AND s.semester=? AND a.subject=? AND a.date=? " +
                "ORDER BY a.roll_no");
            ps.setString(1, stream);
            ps.setString(2, cls);
            ps.setString(3, sem);
            ps.setString(4, subject);
            ps.setString(5, date);

            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-20s %-10s\n", "Roll No", "Name", "Status"));
            sb.append("----------------------------------\n");

            while (rs.next()) {
                sb.append(String.format("%-10s %-20s %-10s\n", 
                    rs.getString("roll_no"), 
                    rs.getString("name"), 
                    rs.getString("status").equals("P") ? "Present" : "Absent"));
            }

            if (sb.length() == 0) {
                resultArea.setText("No attendance records found.");
            } else {
                resultArea.setText(sb.toString());
            }
        } catch (Exception ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void loadMonthlySummary() {
        String stream = streamMS.getSelectedItem();
        String cls = classMS.getSelectedItem();
        String sem = semMS.getSelectedItem();
        String subject = subjectMS.getSelectedItem();
        String yearMonth = new SimpleDateFormat("yyyy-MM").format(new java.util.Date());

        summaryArea.setText("");

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT a.roll_no, s.name, " +
                "COUNT(*) AS total, " +
                "SUM(CASE WHEN a.status='P' THEN 1 ELSE 0 END) AS present " +
                "FROM attendance a JOIN students s ON a.roll_no = s.roll_no " +
                "WHERE s.stream=? AND s.class=? AND s.semester=? AND a.subject=? AND a.date LIKE ? " +
                "GROUP BY a.roll_no, s.name " +
                "ORDER BY a.roll_no");
            ps.setString(1, stream);
            ps.setString(2, cls);
            ps.setString(3, sem);
            ps.setString(4, subject);
            ps.setString(5, yearMonth + "%");

            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("Monthly Summary for ").append(subject).append(" (").append(yearMonth).append(")\n\n");
            sb.append(String.format("%-10s %-20s %-10s %-10s %-10s\n", 
                "Roll No", "Name", "Present", "Total", "%"));
            sb.append("----------------------------------------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                String roll = rs.getString("roll_no");
                String name = rs.getString("name");
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                int percent = (int)((present * 100.0) / total);
                sb.append(String.format("%-10s %-20s %-10d %-10d %-10d\n", 
                    roll, name, present, total, percent));
            }

            if (!found) {
                summaryArea.setText("No records found for this month.");
            } else {
                summaryArea.setText(sb.toString());
            }
        } catch (Exception ex) {
            summaryArea.setText("Error: " + ex.getMessage());
        }
    }

    private void viewTodaysAttendance() {
        String roll = txtRollNo.getText().trim();
        if (roll.isEmpty()) {
            showError("Please enter Roll Number");
            return;
        }

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT subject, status FROM attendance WHERE roll_no=? AND date=? ORDER BY subject");
            ps.setString(1, roll);
            ps.setString(2, today);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("Today's Attendance (").append(today).append(")\n\n");
            sb.append(String.format("%-15s %-10s\n", "Subject", "Status"));
            sb.append("-----------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                String subject = rs.getString("subject");
                String status = rs.getString("status");
                sb.append(String.format("%-15s %-10s\n", 
                    subject, status.equals("P") ? "Present" : "Absent"));
            }

            if (!found) {
                showDialog("No attendance found for today.");
            } else {
                showDialog(sb.toString());
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void viewStudentMonthlySummary() {
        String roll = txtRollNo.getText().trim();
        if (roll.isEmpty()) {
            showError("Please enter Roll Number");
            return;
        }

        String yearMonth = new SimpleDateFormat("yyyy-MM").format(new java.util.Date());

        try {
            // Get student name first
            String studentName = "";
            PreparedStatement nameStmt = conn.prepareStatement(
                "SELECT name FROM students WHERE roll_no = ?");
            nameStmt.setString(1, roll);
            ResultSet nameRs = nameStmt.executeQuery();
            if (nameRs.next()) {
                studentName = nameRs.getString("name");
            }

            PreparedStatement ps = conn.prepareStatement(
                "SELECT subject, " +
                "COUNT(*) AS total, " +
                "SUM(CASE WHEN status='P' THEN 1 ELSE 0 END) AS present " +
                "FROM attendance " +
                "WHERE roll_no=? AND date LIKE ? " +
                "GROUP BY subject " +
                "ORDER BY subject");
            ps.setString(1, roll);
            ps.setString(2, yearMonth + "%");
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("Monthly Attendance Summary for ").append(roll);
            if (!studentName.isEmpty()) sb.append(" - ").append(studentName);
            sb.append("\n(").append(yearMonth).append(")\n\n");
            sb.append(String.format("%-15s %-10s %-10s %-10s\n", "Subject", "Present", "Total", "%"));
            sb.append("----------------------------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                String subject = rs.getString("subject");
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                int percentage = (int) ((present * 100.0f) / total);
                sb.append(String.format("%-15s %-10d %-10d %-10d\n", 
                    subject, present, total, percentage));
            }

            if (!found) {
                showDialog("No attendance records found for this month.");
            } else {
                showDialog(sb.toString());
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void clearStudentList() {
        studentListHolder.removeAll();
        studentListHolder.revalidate();
        studentListHolder.repaint();
    }

    private void showDialog(String msg) {
        Dialog d = new Dialog(this, "Message", true);
        d.setLayout(new FlowLayout());
        d.setSize(300, 150);
        TextArea ta = new TextArea(msg, 5, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false);
        d.add(ta);
        Button ok = new Button("OK");
        ok.addActionListener(e -> d.setVisible(false));
        d.add(ok);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void showError(String msg) {
        Dialog d = new Dialog(this, "Error", true);
        d.setLayout(new FlowLayout());
        d.setSize(300, 120);
        Label label = new Label(msg);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        d.add(label);
        Button b = new Button("OK");
        b.addActionListener(e -> d.setVisible(false));
        d.add(b);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        new attendanceAWT();
    }
}