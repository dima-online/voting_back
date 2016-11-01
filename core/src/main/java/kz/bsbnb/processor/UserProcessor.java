package kz.bsbnb.processor;

import kz.bsbnb.common.model.User;
import org.springframework.stereotype.Service;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
public interface UserProcessor {

    void mergeUser(User user);

}
