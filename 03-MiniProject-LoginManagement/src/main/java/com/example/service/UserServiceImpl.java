package com.example.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Place;
import com.example.entity.User;
import com.example.repo.PlaceRepo;
import com.example.repo.UserRepo;
import com.example.util.PasswordGenerator;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PlaceRepo placeRepo;

	@Autowired
	private UserRepo userRepo;

	@Override
	public boolean validateEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	@Override
	public List<String> getCountries() {
		List<String> countryList = new LinkedList<>();
		List<Place> places = placeRepo.findAll();
		for (Place place : places) {
			if (!countryList.contains(place.getCountry())) {
				countryList.add(place.getCountry());
			}
		}
		return countryList;
	}

	public Map<String, Map<String, List<String>>> getDropdownData() {

		Map<String, List<String>> statemap = new LinkedHashMap<>();
		Map<String, String> statemap_check = new LinkedHashMap<>();
		Map<String, Map<String, List<String>>> countrymap = new LinkedHashMap<String, Map<String, List<String>>>();

		List<Place> placeList = placeRepo.findAll();
		List<String> stateList = new ArrayList<>();

		for (int i = 0; i < placeList.size(); i++) {
			if (statemap.containsKey(placeList.get(i).getState())) {
				List<String> cityList = statemap.get(placeList.get(i).getState());
				cityList.add(placeList.get(i).getCity());
				statemap.put(placeList.get(i).getState(), cityList);
				statemap_check.put(placeList.get(i).getState(), placeList.get(i).getCountry());
			} else {
				List<String> cityList = new ArrayList<>();
				cityList.add(placeList.get(i).getCity());
				statemap.put(placeList.get(i).getState(), cityList);
				statemap_check.put(placeList.get(i).getState(), placeList.get(i).getCountry());
			}

			if (!stateList.contains(placeList.get(i).getState())) {
				stateList.add(placeList.get(i).getState());
			}
		}

		for (int i = 0; i < stateList.size(); i++) {
			String state_key = stateList.get(i);
			String country_key = statemap_check.get(state_key);
			
			if (countrymap.containsKey(country_key)) {
				Map<String, List<String>> temp_map = countrymap.get(country_key);
				List<String> cityList = statemap.get(state_key);
				temp_map.put(state_key, cityList);
				countrymap.put(country_key, temp_map);
			} else {
				Map<String, List<String>> temp_map = new LinkedHashMap<>();
				List<String> cityList = statemap.get(state_key);
				temp_map.put(state_key, cityList);
				countrymap.put(country_key, temp_map);
			}
		}

		for (Map.Entry<String, Map<String, List<String>>> entrySet : countrymap.entrySet()) {
			System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
		}
		return countrymap;
	}

	@Override
	public String registerUser(User user) {
		String value = "";
		if (user.getId() == null) {
			String generateTemporaryPassword = PasswordGenerator.generateTemporaryPassword();
			user.setIsLocked("Y");
			user.setTempPassword(generateTemporaryPassword);
			value = "inserted";
		} else {
			user.setIsLocked("N");
			value = "updated";
		}
		userRepo.save(user);
		return value;
	}

	public User findUserById(Integer id) {
		Optional<User> findById = userRepo.findById(id);
		if (findById.isPresent()) {
			return findById.get();
		}
		return null;
	}

	@Override
	public User findUserByMail(String email) {
		return userRepo.findByEmail(email);
	}

	public Optional<User> checkLoginUser(String email, String password) {
		return userRepo.findByEmailAndConfirmPassword(email, password);
	}

}
