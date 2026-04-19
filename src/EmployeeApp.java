import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EmployeeApp extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private String userRole;

    public EmployeeApp(String role) {
        this.userRole = role;
        DatabaseHelper.init();

        setTitle("Employee System - " + role);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Таблица
        String[] cols = {"ID", "Name", "Email", "Position", "Score", "Rating", "Salary"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        loadData();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Панель управления
        JPanel panel = new JPanel();
        JButton btnAdd = new JButton("Add Employee");
        JButton btnDel = new JButton("Delete");

        // Ограничение прав (Role-based access)
        if (!role.equals("Admin")) {
            btnAdd.setEnabled(false);
            btnDel.setEnabled(false);
        }

        panel.add(btnAdd);
        panel.add(btnDel);
        add(panel, BorderLayout.SOUTH);

        // Логика кнопок
        btnAdd.addActionListener(e -> showAddDialog());
        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                DatabaseHelper.deleteEmployee(tableModel.getValueAt(row, 0).toString());
                loadData();
            }
        });

        setVisible(true);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            while (rs.next()) {
                int score = rs.getInt("score");
                String rating = (score >= 90) ? "Excellent" : (score >= 75 ? "Good" : "Fair");
                tableModel.addRow(new Object[]{
                        rs.getString("id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("pos"), score, rating, rs.getDouble("salary")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void showAddDialog() {
        // Здесь можно создать маленькое окно с полями JTextField для ввода
        // Для краткости: вызываем ввод через JOptionPane
        String id = JOptionPane.showInputDialog("Enter ID:");
        String name = JOptionPane.showInputDialog("Enter Name:");
        String email = JOptionPane.showInputDialog("Enter Email:");
        // Простая валидация
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid Email!");
            return;
        }
        int score = Integer.parseInt(JOptionPane.showInputDialog("Score (1-100):"));
        double sal = Double.parseDouble(JOptionPane.showInputDialog("Salary:"));

        DatabaseHelper.addEmployee(new FullTimeEmployee(id, name, email, "Staff", score, sal), "FullTime");
        loadData();
    }

    public static void main(String[] args) {
        // Окно входа (Auth)
        String user = JOptionPane.showInputDialog("Username:");
        String pass = JOptionPane.showInputDialog("Password:");

        if ("admin".equals(user) && "admin123".equals(pass)) {
            new EmployeeApp("Admin");
        } else {
            new EmployeeApp("User");
        }
    }
}