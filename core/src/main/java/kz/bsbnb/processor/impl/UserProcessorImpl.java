package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.UserMapper;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.util.ObjectMapperUtil;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
@Service
public class UserProcessorImpl implements UserProcessor {

    @Autowired
    private IUserRoleRepository userRoleRepository;
    @Autowired
    private IUserRepository userRepository;

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

    public List<UserMapper> getUsers(int page, int count) {
        List<User> users = userRepository.findAll(new PageRequest(page,count)).getContent();
        List<UserMapper> result = new ArrayList<>();
        for(User u : users) {
            result.add(userMapper(u));
        }

        return result;
    }
}

