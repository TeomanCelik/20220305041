package ATM;

public class User {
    String name;
    String surname;
    int pin;
    double balance;

    public User(String name, String surname, int pin, double balance) {
        this.name = name;
        this.surname = surname;
        this.pin = pin;
        this.balance = balance;
    }
}