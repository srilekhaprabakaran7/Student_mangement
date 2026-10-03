public class Student {
    

 private String id;
    private String name;
    private int age;

    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getters and Setters (Encapsulation)
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }

    // Convert object to a file-friendly string format
    @Override
    public String toString() {
        return  id + "," + name + "," + age;
    }
}
