import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// Assuming Student.java is in the same package or imported correctly


// ==========================================
// 2. MAIN APPLICATION CLASS
// ==========================================
public class StudentManagement {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("================================================");
        System.out.println(" Welcome to the Student Management System!");
        System.out.println("================================================");
        // Main Menu Loop
        while (running) {
            System.out.println("\n1. Save (Add) Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            
            String choice = scanner.nextLine();

            // Menu Routing using if-else if instead of switch-case
            if (choice.equals("1")) {
                saveStudent(scanner);
            } else if (choice.equals("2")) {
                viewAllStudents();
            } else if (choice.equals("3")) {
                updateStudent(scanner);
            } else if (choice.equals("4")) {
                deleteStudent(scanner);
            } else if (choice.equals("5")) {
                running = false;
                System.out.println("Exiting application. Goodbye!");
            } else {
                System.out.println("Invalid option! Please enter a number between 1 and 5.");
            }
        }
        scanner.close();
    }

    // ==========================================
    // FILE HANDLING OPERATIONS (CRUD)
    // ==========================================

    // SAVE (CREATE): Appends a new student to the file
    private static void saveStudent(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        Student student = new Student(id, name, age);

        // Open in append mode (true) using FileWriter and BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(student.toString());
            writer.newLine();
            System.out.println("✔ Student details saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }

    // VIEW (READ): Reads all student logs from the file
    private static void viewAllStudents() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No records found. The database file is currently empty.");
            return;
        }

        System.out.println("\n--- Registered Students ---");
        System.out.printf("%-10s %-20s %-5s\n", "ID", "Name", "Age");
        System.out.println("-------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split comma-separated tokens
                String[] details = line.split(",");
                if (details.length == 3) {
                    System.out.printf("%-10s %-20s %-5s\n", details[0], details[1], details[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading student records: " + e.getMessage());
        }
    }

    // UPDATE: Finds a student record by ID and modifies details
    private static void updateStudent(Scanner scanner) {
        System.out.print("Enter the ID of the student to update: ");
        String targetId = scanner.nextLine();
        
        List<Student> studentList = loadAllFromFile();
        boolean found = false;

        for (Student student : studentList) {
            if (student.getId().equalsIgnoreCase(targetId)) {
                System.out.print("Enter New Name (Current: " + student.getName() + "): ");
                student.setName(scanner.nextLine());
                System.out.print("Enter New Age (Current: " + student.getAge() + "): ");
                student.setAge(Integer.parseInt(scanner.nextLine()));
                found = true;
                break;
            }       
        }

        if (found) {
            saveAllToFile(studentList);
            System.out.println("✔ Student records updated successfully!");
        } else {
            System.out.println("❌ Student ID not found.");
        }
    }

    // DELETE: Removes a target record by excluding it from file rewrites
    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter the ID of the student to delete: ");
        String targetId = scanner.nextLine();

        List<Student> studentList = loadAllFromFile();
        int initialSize = studentList.size();

        // Use Lambda/OOP filter or standard iteration to remove target item
        studentList.removeIf(student -> student.getId().equalsIgnoreCase(targetId));

        if (studentList.size() < initialSize) {
            saveAllToFile(studentList);
            System.out.println("✔ Student profile removed successfully!");
        } else {
            System.out.println("❌ Student ID not found.");
        }
    }

    // ==========================================
    // UTILITY METHODS FOR FILE BUFFERING
    // ==========================================
    
    // Reads whole database file to memory for complex manipulation (Update/Delete)
    private static List<Student> loadAllFromFile() {
        List<Student> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3) {
                    list.add(new Student(details[0], details[1], Integer.parseInt(details[2])));
                }
            }
        } catch (IOException e) {
            System.out.println("File loading error: " + e.getMessage());
        }
        return list;
    }

    // Overwrites the text file with modified collection states
    private static void saveAllToFile(List<Student> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) { // false = overwrite
            for (Student s : list) {
                writer.write(s.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("File overwriting error: " + e.getMessage());
        }
    }
}