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

        mapper.setIdn(userInfo.getIdn());
        mapper.setPhone(userInfo.getPhone());
        mapper.setEmail(userInfo.getEmail());
        mapper.setStatus(userInfo.getStatus());
        mapper.setEmailNotification(userInfo.getEmailNotification());
        mapper.setSmsNotification(userInfo.getSmsNotification());
        return mapper;
    }
}
