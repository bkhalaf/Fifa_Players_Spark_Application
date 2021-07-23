public class Player {
    String Name;
    int Age;
    String Nationality;
    int Score;
    String Club;
    float NumValue;
    float NumSalary;
    String Continent;

    public Player() {
    }

    public Player(String name, int age, String nationality, int score, String club, float numValue, float numSalary, String continent) {
        Name = name;
        Age = age;
        Nationality = nationality;
        Score = score;
        Club = club;
        NumValue = numValue;
        NumSalary = numSalary;
        Continent = continent;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public String getClub() {
        return Club;
    }

    public void setClub(String club) {
        Club = club;
    }

    public float getNumValue() {
        return NumValue;
    }

    public void setNumValue(float numValue) {
        NumValue = numValue;
    }

    public float getNumSalary() {
        return NumSalary;
    }

    public void setNumSalary(float numSalary) {
        NumSalary = numSalary;
    }

    public String getContinent() {
        return Continent;
    }

    public void setContinent(String continent) {
        Continent = continent;
    }
}
