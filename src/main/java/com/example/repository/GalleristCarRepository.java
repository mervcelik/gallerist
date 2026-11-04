package com.example.repository;

import com.example.model.GalleristCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleristCarRepository extends JpaRepository<GalleristCar, Long> {

    boolean existsByGalleristIdAndCarId(Long galleristId, Long carId);

    boolean existsByGalleristIdAndCarIdAndIdNot(Long galleristId, Long carId, Long id);
}
