import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

enum Role {
    ADMIN("Administrator"),
    TEACHER("Teacher"),
    STUDENT("Student");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

enum Department {
    COMPUTER_SCIENCE("CS", "Computer Science"),
    SOFTWARE_ENGINEERING("SE", "Software Engineering"),
    BUSINESS_ADMIN("BBA", "Business Administration"),
    ELECTRICAL_ENG("EE", "Electrical Engineering"),
    MECHANICAL_ENG("ME", "Mechanical Engineering");

    private final String code;
    private final String fullName;

    Department(String code, String fullName) {
        this.code = code;
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }
}

class Student {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Department department;
    private LocalDate enrollmentDate;
    private String address;

    public Student(String studentId, String firstName, String lastName,
                   String email, String phone, LocalDate dateOfBirth,
                   Department department, String address) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.department = department;
        this.enrollmentDate = LocalDate.now();
        this.address = address;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Department getDepartment() { return department; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public String getAddress() { return address; }
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
}

class Course {
    private String courseId;
    private String courseName;
    private String courseCode;
    private Department department;
    private int credits;
    private String description;

    public Course(String courseId, String courseName, String courseCode,
                  Department department, int credits, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.department = department;
        this.credits = credits;
        this.description = description;
    }

    // Getters
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getCourseCode() { return courseCode; }
    public Department getDepartment() { return department; }
    public int getCredits() { return credits; }
    public String getDescription() { return description; }
}

class DataRepository {
    private Map<String, Student> students = new HashMap<>();
    private Map<String, Course> courses = new HashMap<>();

    public void addStudent(Student student) {
        students.put(student.getStudentId(), student);
    }

    public Student getStudent(String studentId) {
        return students.get(studentId);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public void deleteStudent(String studentId) {
        students.remove(studentId);
    }

    public void addCourse(Course course) {
        courses.put(course.getCourseId(), course);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public Course getCourse(String courseId) {
        return courses.get(courseId);
    }
}

public class StudentManagementSystem extends JFrame {
    private Role currentRole = Role.ADMIN;
    private DataRepository repository = new DataRepository();

    private DefaultTableModel studentTableModel;
    private DefaultTableModel courseTableModel;
    private JTable studentTable;
    private JTable courseTable;

    public StudentManagementSystem() {
        initializeUI();
        loadSampleData();
        refreshStudentTable();
        refreshCourseTable();
    }

    private void initializeUI() {
        setTitle("Student Management System - " + currentRole.getDisplayName());
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("👨‍🎓 Students", createStudentsPanel());
        tabbedPane.addTab("📚 Courses", createCoursesPanel());
        tabbedPane.addTab("📊 Dashboard", createSimpleDashboardPanel());

        add(tabbedPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 152, 219));
        header.setPreferredSize(new Dimension(getWidth(), 70));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("🎓 Student Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Welcome, " + currentRole.getDisplayName());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(exitBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createSimpleDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(createInfoCard("Total Students", String.valueOf(repository.getAllStudents().size())));
        panel.add(createInfoCard("Total Courses", String.valueOf(repository.getAllCourses().size())));
        panel.add(createInfoCard("Active Enrollments", "0")); // Placeholder
        panel.add(createInfoCard("Today's Date", LocalDate.now().toString()));

        return panel;
    }

    private JPanel createInfoCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(new Color(52, 152, 219));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
        return card;
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("➕ Add Student");
        JButton deleteBtn = new JButton("🗑️ Delete Selected");
        JButton refreshBtn = new JButton("🔄 Refresh");

        addBtn.addActionListener(e -> showAddStudentDialog());
        deleteBtn.addActionListener(e -> deleteSelectedStudent());
        refreshBtn.addActionListener(e -> refreshStudentTable());

        toolbar.add(addBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);

        // Table
        String[] columns = {"ID", "Name", "Email", "Department", "Phone", "Age", "Enrollment Date"};
        studentTableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(35);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("➕ Add Course");
        JButton refreshBtn = new JButton("🔄 Refresh");

        addBtn.addActionListener(e -> showAddCourseDialog());
        refreshBtn.addActionListener(e -> refreshCourseTable());

        toolbar.add(addBtn);
        toolbar.add(refreshBtn);

        String[] columns = {"Course ID", "Code", "Name", "Department", "Credits"};
        courseTableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(courseTableModel);
        courseTable.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(courseTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add New Student", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(9, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTextField idField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField dobField = new JTextField("2000-01-01"); // Format: YYYY-MM-DD
        JComboBox<Department> deptCombo = new JComboBox<>(Department.values());

        form.add(new JLabel("Student ID:"));
        form.add(idField);
        form.add(new JLabel("First Name:"));
        form.add(firstNameField);
        form.add(new JLabel("Last Name:"));
        form.add(lastNameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        form.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        form.add(dobField);
        form.add(new JLabel("Department:"));
        form.add(deptCombo);

        JPanel buttons = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save Student");
        JButton cancelBtn = new JButton("Cancel");

        cancelBtn.addActionListener(e -> dialog.dispose());
        saveBtn.addActionListener(e -> {
            try {
                Student student = new Student(
                        idField.getText().trim(),
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneField.getText().trim(),
                        LocalDate.parse(dobField.getText().trim()),
                        (Department) deptCombo.getSelectedItem(),
                        "N/A"
                );
                repository.addStudent(student);
                refreshStudentTable();
                JOptionPane.showMessageDialog(dialog, "Student added successfully!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddCourseDialog() {
        JDialog dialog = new JDialog(this, "Add New Course", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTextField idField = new JTextField();
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<Department> deptCombo = new JComboBox<>(Department.values());
        JTextField creditsField = new JTextField();
        JTextField descField = new JTextField();

        form.add(new JLabel("Course ID:")); form.add(idField);
        form.add(new JLabel("Course Code:")); form.add(codeField);
        form.add(new JLabel("Course Name:")); form.add(nameField);
        form.add(new JLabel("Department:")); form.add(deptCombo);
        form.add(new JLabel("Credits:")); form.add(creditsField);
        form.add(new JLabel("Description:")); form.add(descField);

        JButton saveBtn = new JButton("Save Course");
        saveBtn.addActionListener(e -> {
            try {
                Course course = new Course(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        codeField.getText().trim(),
                        (Department) deptCombo.getSelectedItem(),
                        Integer.parseInt(creditsField.getText().trim()),
                        descField.getText().trim()
                );
                repository.addCourse(course);
                refreshCourseTable();
                JOptionPane.showMessageDialog(dialog, "Course added successfully!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(saveBtn);
        bottom.add(new JButton("Cancel") {{ addActionListener(e -> dialog.dispose()); }});

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedStudent() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
            return;
        }
        String id = (String) studentTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete student " + id + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            repository.deleteStudent(id);
            refreshStudentTable();
            JOptionPane.showMessageDialog(this, "Student deleted.");
        }
    }

    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : repository.getAllStudents()) {
            studentTableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getFullName(),
                    s.getEmail(),
                    s.getDepartment().getFullName(),
                    s.getPhone(),
                    s.getAge(),
                    s.getEnrollmentDate()
            });
        }
    }

    private void refreshCourseTable() {
        courseTableModel.setRowCount(0);
        for (Course c : repository.getAllCourses()) {
            courseTableModel.addRow(new Object[]{
                    c.getCourseId(),
                    c.getCourseCode(),
                    c.getCourseName(),
                    c.getDepartment().getFullName(),
                    c.getCredits()
            });
        }
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bar.setBackground(Color.WHITE);

        JLabel status = new JLabel("Ready | Total Students: " + repository.getAllStudents().size());
        JLabel time = new JLabel();
        time.setHorizontalAlignment(SwingConstants.RIGHT);

        new Timer(1000, e -> time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .start();

        bar.add(status, BorderLayout.WEST);
        bar.add(time, BorderLayout.EAST);
        return bar;
    }

    private void loadSampleData() {
        repository.addStudent(new Student("STU001", "John", "Doe", "john@university.edu",
                "123-456-7890", LocalDate.of(2000, 5, 15), Department.COMPUTER_SCIENCE, "123 Main St"));

        repository.addStudent(new Student("STU002", "Jane", "Smith", "jane@university.edu",
                "987-654-3210", LocalDate.of(1999, 8, 22), Department.SOFTWARE_ENGINEERING, "456 Oak Ave"));

        repository.addCourse(new Course("C001", "Introduction to Programming", "CS101",
                Department.COMPUTER_SCIENCE, 3, "Basic programming concepts"));

        repository.addCourse(new Course("C002", "Data Structures", "CS201",
                Department.COMPUTER_SCIENCE, 4, "Advanced data structures"));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("Component.focusWidth", 1);
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }
}