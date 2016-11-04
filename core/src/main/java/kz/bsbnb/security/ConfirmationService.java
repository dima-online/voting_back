package kz.bsbnb.security;

import kz.bsbnb.common.bean.ConfirmBean;
import org.springframework.stereotype.Service;

/**
 * Created by Ruslan on 03.11.2016.
 */
@Service
public class ConfirmationService  {

    public boolean check(ConfirmBean bean) {
        //TODO Проверка данных
        return true;
    }
}
