package kz.bsbnb.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Olzhas.Pazyldayev on 20.11.2017.
 */
@Getter
@Setter
public class Login {

    private String username;
    private String password;
    private Boolean mobile = false;

    public Login() {
    }
}
