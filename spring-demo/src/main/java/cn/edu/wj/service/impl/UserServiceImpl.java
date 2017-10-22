package cn.edu.wj.service.impl;

import org.springframework.stereotype.Service;

import cn.edu.wj.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Override
	public void addUser() {
		System.out.println("添加User");
	}

}
