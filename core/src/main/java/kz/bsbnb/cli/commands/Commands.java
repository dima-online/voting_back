package kz.bsbnb.cli.commands;

import kz.bsbnb.common.model.impl.user.User;
import kz.bsbnb.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Timur.Abdykarimov on 31.08.2016.
 */

@Component
@Import(User.class)
public class Commands implements CommandMarker {

    @Autowired
    private IUserRepository users;

    public void setUsers(IUserRepository users) {
        this.users = users;
    }

    @CliAvailabilityIndicator({"getusers"})
    public boolean isGetUsersAvailable(){
        return true;
    }

    @CliAvailabilityIndicator({"getuser"})
    public boolean isGetUserAvailable(){
        return true;
    }

    @CliAvailabilityIndicator({"newuser"})
    public boolean isCreateUsersAvailable(){
        return true;
    }

    @CliCommand(value = "getuser", help = "User by username")
    public String getUser(@CliOption(key = {""},mandatory = true, help = "enter Username") final String username)
    {
        try {

            User user = users.findByUsername(username);

            String str = new String();
            Iterator<? extends GrantedAuthority> userRoleIterator = user.getAuthorities().iterator();
            str = "[";
            while (userRoleIterator.hasNext()){
                str = str + " " + userRoleIterator.next().getAuthority();
            }
            str = str + "]";

            return "Username = " + user.getUsername() + " iin  " + user.getIin() + " roles " + str;
        }catch (NullPointerException npe){
            return "user not found";
        }

    }

    @CliCommand(value = "getusers",help = "Get list users")
    public List<User> getusers(){
        List<User> result = new ArrayList<>();
        users.findAll().forEach(result::add);
        return result;
    }

    @CliCommand(value = "newuser", help = "Create new user")
    public String newUser(
            @CliOption(key = {"username"}   ,mandatory = true   ,help = "username") final String username,
            @CliOption(key = {"iin"}        ,mandatory = true   ,help = "iin") final String iin,
            @CliOption(key = {"pass"}       ,mandatory = true   ,help = "") final String pass
    ){
        // add role
        User user = new User();
        user.setUsername(username);
        user.setIin(iin);
        user.setPassword(pass);
        users.save(user);

        return "user added: name = " + user.getUsername() + "; iin = " + user.getIin() ;
    }
}
