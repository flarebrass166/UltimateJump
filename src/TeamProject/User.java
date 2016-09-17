package TeamProject;

public class User <K, V extends Comparable<V>> implements Comparable<User <K,V>>{

    private String userName;
    private Integer score;

    public User(String userName, Integer score) {
        super();
        this.score = score;
        this.userName = userName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void Score(){
        score++;
    }
    public String toString(){
        return String.format("%s  %d", getUserName(), getScore() );
    }


    @Override
    public int compareTo(User<K, V> o) {
        return this.getScore().compareTo(o.getScore());
    }
}