package Player;


import java.rmi.Remote;

public interface IAccount extends Remote {

    /**
    Getter und Setter
     */
    public void setName(String name);

    public String getName();

    public void setPassword(String pw);

    public String getPassword();
}
