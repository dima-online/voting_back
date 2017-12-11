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
    @SequenceGenerator(name = "core.user_notification_id_seq", sequenceName = "core.user_notification_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.user_notification_id_seq")
    Long id;

}
