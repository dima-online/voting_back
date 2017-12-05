package kz.bsbnb.processor;

import kz.bsbnb.common.bean.UserMapper;
import kz.bsbnb.common.model.User;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
public interface UserProcessor {

    void mergeUser(User user);

    UserMapper userMapper(User user);

}
