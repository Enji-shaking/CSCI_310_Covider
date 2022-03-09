package database.user;

public class User {
    // incorrect approach since id_inc is reset everytime the program restarts
    //    private static int id_inc = 1;
    private int ID;
    private String name;
    private String password;
    private int isStudent; // 1 or 0

//    public static User getNewUser(String name, String password, int isStudent){
//        return new User(id_inc++, name, password, isStudent);
//    }

    public User(int ID, String name, String password, int isStudent) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.isStudent = isStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(int isStudent) {
        this.isStudent = isStudent;
    }

}
