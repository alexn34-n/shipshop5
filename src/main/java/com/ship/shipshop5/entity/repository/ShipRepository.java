package com.ship.shipshop5.entity.repository;



import com.ship.shipshop5.entity.Ship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ShipRepository extends CrudRepository<Ship, UUID>, JpaSpecificationExecutor<Ship> {
    List<Ship> findAll();
}
