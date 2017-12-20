package kz.bsbnb.common.model;

import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by serik.mukashev on 09.12.2017.
 */
@Entity
@Table(name = "user_notification", schema = Constants.DB_SCHEMA_CORE)
public class UserNotification implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

}
