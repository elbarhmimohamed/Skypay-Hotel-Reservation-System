package services;

import entities.Room;
import entities.User;
import exceptions.NoEntryFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static utils.Constants.BACK_TO_LINE;
import static utils.Constants.USER_HEADER;
import static utils.Constants.USER_SEPARATOR;

public class UserService {

    List<User> users = new ArrayList<>();
    public List<User> getAllUsers(){
        return users;
    }
    public int getUsersCount(){
        return users.size();
    }

    public User findUserById(int userId){

        Optional<User> user = users.stream().filter(u -> u.getId() == userId).findFirst();

        if (!user.isPresent()){
            throw new NoEntryFoundException(String.format("User with id %d not found.", userId));
        }

        return user.get();
    }

    void setUser(int userId, int balance){

        User user = users.stream().filter(u -> u.getId() == userId)
                .findFirst().orElse(null);

        if(user == null){
            user = new User(userId, balance);
            users.add(user);
        }
        else{
            user.setBalance(balance);
        }
    }

    void printAllUsers(){

        if(this.users.isEmpty()){
            throw new NoEntryFoundException("No User Found");
        }

        StringBuilder result = new StringBuilder(USER_HEADER);

        for (int i = users.size() - 1 ; i >= 0 ; i--) {

            result.append(users.get(i).getId()).append(USER_SEPARATOR);
            result.append(users.get(i).getBalance()).append(BACK_TO_LINE);
        }

        System.out.println(result.toString());

    }
}
