import java.io.Serializable;

public abstract class Employee implements Serializable {
    private String id;
    private String name;
    private String email;
    private String position;
    private int performanceScore; // Новая функция: Оценка (1-100)

    public Employee(String id, String name, String email, String position, int score) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.position = position;
        this.performanceScore = score;
    }

    public String getRating() {
        if (performanceScore >= 90) return "Excellent";
        if (performanceScore >= 75) return "Good";
        return "Satisfactory";
    }

    public abstract double calculateSalary(); // Абстрактный метод

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPosition() { return position; }
    public int getPerformanceScore() { return performanceScore; }
}

class FullTimeEmployee extends Employee {
    private double salary;
    public FullTimeEmployee(String id, String name, String email, String pos, int score, double salary) {
        super(id, name, email, pos, score);
        this.salary = salary;
    }
    @Override public double calculateSalary() { return salary; }
}
