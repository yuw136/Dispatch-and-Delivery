package com.flagcamp.dispatchanddelivery.repository;

import com.flagcamp.dispatchanddelivery.entity.RobotEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RobotRepository extends ListCrudRepository<RobotEntity, Long> {

    List<RobotEntity> findByAvailableTrue();

    @Query("SELECT * FROM robots WHERE hub_id = :hubId AND available = true AND robot_type = :type")
    List<RobotEntity> findAvailableByHubIdAndType(Long hubId, String type);

}
