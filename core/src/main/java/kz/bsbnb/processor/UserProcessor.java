package kz.bsbnb.processor;

import kz.bsbnb.common.model.Role;
import kz.bsbnb.common.model.impl.user.User;
import kz.bsbnb.common.model.impl.user.UserRole;
import kz.bsbnb.repository.IUserDocTypeRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
public interface UserProcessor {

    void mergeUser(User user);

}
