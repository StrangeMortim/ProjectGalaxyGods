package Player;


import java.io.Serializable;

public class Account implements IAccount,Serializable {

    private String name;
    private String password;

    public Account(String name, String password){
        if(name.equals("") || password.equals(""))
            throw new IllegalArgumentException("Either name or password is empty");

        this.name = name;
        this.password = password;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPassword(String pw) {
        this.password = pw;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
