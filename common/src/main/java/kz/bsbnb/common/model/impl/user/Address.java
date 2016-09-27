package kz.bsbnb.common.model.impl.user;

import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;

/**
 * Created by Olzhas.Pazyldayev on 06.09.2016.
 */
@Entity
@Table(
        name = "user_address",
        schema = Constants.DB_SCHEMA_CORE
)
public class Address implements IPersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 20)
    private String postCode; //"050004"
    @Column(length = 2)
    private String countryCode; //"KZ",
    private String region; //"Алматы",
    private String area; //"Алматинская область",
    private String typeCity; //"5",
    @Column(length = 500)
    private String nameCity; //"Алматы",
    @Column(length = 500)
    private String addressCity; //"ул.Пушкина 2",

    public Address() {
    }

    public Address(String postCode, String countryCode, String region, String area, String typeCity, String nameCity, String addressCity) {
        this.postCode = postCode;
        this.countryCode = countryCode;
        this.region = region;
        this.area = area;
        this.typeCity = typeCity;
        this.nameCity = nameCity;
        this.addressCity = addressCity;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTypeCity() {
        return typeCity;
    }

    public void setTypeCity(String typeCity) {
        this.typeCity = typeCity;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

}
