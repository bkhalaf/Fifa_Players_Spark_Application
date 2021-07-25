public class hiveTable {
    String  Name;
    int Age;
    String Nationality;
    String Continent;
    int Score;
    String Club;
    float numSalary;
    float numValue;

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

    public String getContinent() {
        return Continent;
    }

    public void setContinent(String continent) {
        Continent = continent;
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

    public float getNumSalary() {
        return numSalary;
    }

    public void setNumSalary(float numSalary) {
        this.numSalary = numSalary;
    }

    public float getNumValue() {
        return numValue;
    }

    public void setNumValue(float numValue) {
        this.numValue = numValue;
    }

    public hiveTable(String name, int age, String nationality, String continent, int score, String club, float numSalary, float numValue) {
        Name = name;
        Age = age;
        Nationality = nationality;
        Continent = continent;
        Score = score;
        Club = club;
        this.numSalary = numSalary;
        this.numValue = numValue;
    }
}
