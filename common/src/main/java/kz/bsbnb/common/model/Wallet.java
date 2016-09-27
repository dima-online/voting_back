package kz.bsbnb.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Olzhas.Pazyldayev on 16.09.2016.
 */
public interface Wallet<T> extends Serializable {

    T getId();

    void setId(T id);
}
