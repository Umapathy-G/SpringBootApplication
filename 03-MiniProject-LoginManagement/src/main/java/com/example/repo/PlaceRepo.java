package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Place;

public interface PlaceRepo extends JpaRepository<Place, Integer> {

}
