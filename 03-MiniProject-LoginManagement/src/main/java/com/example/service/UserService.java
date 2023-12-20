package com.example.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.User;

@Service
public interface UserService {

	public boolean validateEmail(String email);

	public List<String> getCountries();

	public String registerUser(User user);

	public Map<String, Map<String, List<String>>> getDropdownData();

	public User findUserById(Integer id);

	public User findUserByMail(String email);

	public Optional<User> checkLoginUser(String email, String password);
}
