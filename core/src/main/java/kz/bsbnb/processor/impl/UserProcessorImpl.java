package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.UserMapper;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.util.ObjectMapperUtil;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
@Service
public class UserProcessorImpl implements UserProcessor {

    @Autowired
    private IUserRoleRepository userRoleRepository;

    public void mergeUser(User user) {
//        Set<UserRoles> roles = new HashSet<>();
//        user.getAuthorities().forEach(userRole1 -> {
//            UserRoles userRole = userRoleRepository.findByRole(Role.getRole(userRole1.getAuthority()));
//            roles.add(userRole);
//        });
//        user.setUserRolesSet(roles);

    }

    @Override
    public UserMapper userMapper(User user) {
        UserMapper userMapper = new UserMapper();
        userMapper.setId(user.getId());
        userMapper.setStatus(user.getStatus().toString());
        userMapper.setUserInfo(ObjectMapperUtil.userInfoMapper(user.getUserInfo()));
        userMapper.getUserInfo().setIin(user.getIin());
        userMapper.setUsername(user.getIin());
        userMapper.setUserRolesSet(new HashSet<>(userRoleRepository.findByUser(user)));
        userMapper.setExecutiveOfficeIin(user.getExecutiveOfficeIin());
        return userMapper;
    }
}
