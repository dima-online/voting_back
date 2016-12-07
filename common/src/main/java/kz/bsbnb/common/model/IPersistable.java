package kz.bsbnb.common.model;

import java.io.Serializable;

/**
 * Created by Olzhas.Pazyldayev on 12.08.2016.
 */
public interface IPersistable extends Serializable{
    Long getId();

    void setId(Long id);
}
