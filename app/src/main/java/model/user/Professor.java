package model.user;

public class Professor extends User{

    public Professor(int id, String name, String password) {
        super(id, name, password, 0);
    }

    public void notifyOnline(int classId){

    }
}
