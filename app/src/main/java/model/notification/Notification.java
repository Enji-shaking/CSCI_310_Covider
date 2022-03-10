package model.notification;

import java.util.Objects;

public class Notification {
    // incorrect approach since id_inc is reset everytime the program restarts
//    private static int id_inc = 1;
    private long id;
    private long from;
    private long to;
    private int read; // 1 or 0
    private String message;

//    public static Notification getNewNotification(int from, int to, int read, String message) {
//        return new Notification(id_inc++, from, to, read, message);
//    }


    public Notification(long id, long from, long to, int read, String message) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.read = read;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return getId() == that.getId() && Objects.equals(getFrom(), that.getFrom()) && Objects.equals(getTo(), that.getTo()) && Objects.equals(getRead(), that.getRead()) && Objects.equals(getMessage(), that.getMessage());
    }
}
