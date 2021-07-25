public class PlayerSalary {
    String Name;
    float numSalary;

    public PlayerSalary(String name, float numSalary) {
        Name = name;
        this.numSalary = numSalary;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getNumSalary() {
        return numSalary;
    }

    public void setNumSalary(float numSalary) {
        this.numSalary = numSalary;
    }
}
