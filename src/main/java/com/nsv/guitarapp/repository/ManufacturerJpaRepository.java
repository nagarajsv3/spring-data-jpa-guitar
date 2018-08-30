package com.nsv.guitarapp.repository;

import com.nsv.guitarapp.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ManufacturerJpaRepository extends JpaRepository<Manufacturer, Long>{
    List<Manufacturer> findByFoundedDateBefore(Date beforeDate);
    List<Manufacturer> findByActiveTrue();
    List<Manufacturer> findByActiveFalse();
    List<Manufacturer> getAllThatSellAcoustics(String modelType);
}
