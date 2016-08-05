package Player;


import java.io.Serializable;

/**
 * Realises an account for players
 * @author Benjamin
 */
public class Account implements Serializable {

    /**
     * The name of the account
     */
    private String name;

    /**
     * The password of the account
     */
    private String password;

    public Account(String name, String password){
        if(name.equals("") || password.equals(""))
            throw new IllegalArgumentException("Either name or password is empty");

        this.name = name;
        this.password = password;
    }

    /**
     * Getter/setter
     */
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassword(String pw) {
        this.password = pw;
    }
    public String getPassword() {
        return password;
    }
}
