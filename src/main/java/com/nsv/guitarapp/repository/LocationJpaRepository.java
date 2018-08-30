package com.nsv.guitarapp.repository;

import com.nsv.guitarapp.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationJpaRepository extends JpaRepository<Location, Long>{
    List<Location> findByStateIgnoreCaseLike(String state);
    Location findFirstByStateIgnoreCaseLike(String state);
    //List<Location> findDistinctByStateIgnoreCaseLike(String state);

    List<Location> findByStateStartingWith(String state);
    List<Location> findByStateNotLike(String state);
    List<Location> findByStateNotLikeOrderByCountryAsc(String state);
    List<Location> findByStateAndCountry(String state, String country);
    List<Location> findByStateOrCountry(String state, String country);
    List<Location> findByStateIsOrCountryEquals(String state, String country);
    List<Location> findByStateIsOrCountryNot(String state, String country);
    List<Location> findByStateNot(String state);
}
