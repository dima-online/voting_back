package kz.bsbnb.util;

import kz.bsbnb.common.model.impl.user.User;
import kz.bsbnb.repository.IUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Olzhas.Pazyldayev on 13.08.2016.
 */
@Component
public class UserTestDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IUserRepository userRepository;

    private Logger log = Logger.getLogger(UserTestDataLoader.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createUsers();
    }

    public void createUsers() {
        if (userRepository.findByIin("880101123456") == null) {
            User user = new User();
            user.setUsername("bsb.user");
            user.setIin("880101123456");
            //userRepository.save(user);
            log.info("Saved " + user.toString());
        }
        if (userRepository.findByIin("890101123456") == null) {
            User user = new User();
            user.setUsername("bsb.user1");
            user.setIin("890101123456");
            //userRepository.save(user);
            log.info("Saved " + user.toString());
        }
    }
}
