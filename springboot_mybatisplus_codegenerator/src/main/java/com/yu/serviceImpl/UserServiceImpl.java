package com.yu.serviceImpl;

import com.yu.entity.User;
import com.yu.mapper.UserMapper;
import com.yu.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 余刚盛
 * @since 2020-07-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
