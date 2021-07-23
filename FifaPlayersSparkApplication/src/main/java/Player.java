public class Player {
    String Name;
    int Age;
    String Nationality;
    int Score;
    String Club;
    String Value;
    String Salary;
    String Continent;

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

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }

    public String getContinent() {
        return Continent;
    }

    public void setContinent(String continent) {
        Continent = continent;
    }

    public Player(String name, int age, String nationality, int score, String club, String value, String salary, String continent) {
        Name = name;
        Age = age;
        Nationality = nationality;
        Score = score;
        Club = club;
        Value = value;
        Salary = salary;
        Continent = continent;
    }
}
