public class PlayerValues {
    String Name;
    float numValue;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getNumValue() {
        return numValue;
    }

    public void setNumValue(float numValue) {
        this.numValue = numValue;
    }

    public PlayerValues(String name, float numValue) {
        Name = name;
        this.numValue = numValue;
    }
}
