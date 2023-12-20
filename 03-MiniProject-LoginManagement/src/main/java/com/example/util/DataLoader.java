package com.example.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.entity.Place;
import com.example.repo.PlaceRepo;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	private PlaceRepo placeRepo;

	@Override
	public void run(String... args) throws Exception {

		List<Place> list = new ArrayList<>();

		String[] countries = { "India", "India", "India", "India", "India", "India", "India", "United States",
				"United States", "United States", "United States", "United States", "India" };
		String[] states = { "TamilNadu", "TamilNadu", "Karnataka", "Karnataka", "Mumbai", "Mumbai", "Mumbai",
				"California", "Texas", "Florida", "Hawali", "Alaska", "TamilNadu" };
		String[] cities = { "Chennai", "Cuddalore", "Bangalore", "Mysore", "Andheri", "Bandra", "Goregaon",
				"Los Angeles", "Houston", "Miami", "Honolulu", "Anchorage", "Madurai" };

		for (int i = 0; i < countries.length; i++) {
			Place place = new Place();
			place.setCountry(countries[i]);
			place.setState(states[i]);
			place.setCity(cities[i]);
			list.add(place);
		}

		placeRepo.saveAll(list);
	}

}
