package kz.bsbnb.security;

import kz.bsbnb.common.model.User;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Timur.Abdykarimov on 31.08.2016.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    IUserRepository users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails;
        User user = users.findByIin(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        userDetails = new org.springframework.security.core.userdetails.User(
                user.getIin(), user.getPassword(), user.getAuthorities()
        );
        return userDetails;
    }
}
