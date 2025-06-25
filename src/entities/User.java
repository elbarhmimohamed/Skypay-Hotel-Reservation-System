package entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private int id;

    private int balance;


    public User(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public User(User user){
        this.id = user.getId();
        this.balance = user.getBalance();
    }
}
