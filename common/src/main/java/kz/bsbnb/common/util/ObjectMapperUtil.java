package kz.bsbnb.common.util;

import kz.bsbnb.common.bean.UserInfoMapper;
import kz.bsbnb.common.model.UserInfo;

/**
 * Created by Olzhas.Pazyldayev on 04.08.2017.
 */
public class ObjectMapperUtil {
    public static UserInfoMapper userInfoMapper(UserInfo userInfo) {
        UserInfoMapper mapper = new UserInfoMapper();
        mapper.setId(userInfo.getId());
        mapper.setFirstName(userInfo.getFirstName());
        mapper.setLastName(userInfo.getLastName());
        mapper.setMiddleName(userInfo.getMiddleName());
        mapper.setDocumentNumber(userInfo.getDocumentNumber());
        mapper.setIdn(userInfo.getIdn());
        mapper.setPhone(userInfo.getPhone());
        mapper.setEmail(userInfo.getEmail());
        mapper.setEmailNotification(userInfo.getEmailNotification());
        mapper.setSmsNotification(userInfo.getSmsNotification());
        mapper.setDocumentExpireDate(userInfo.getDocumentExpireDate());
        mapper.setDateOfBirth(userInfo.getDateOfBirth());
        mapper.setDocumentGivenDate(userInfo.getDocumentGivenDate());
        mapper.setDocumentGivenAgency(userInfo.getDocumentGivenAgency());
        mapper.setDocumentType(userInfo.getDocumentType().toString());
        return mapper;
    }
}
