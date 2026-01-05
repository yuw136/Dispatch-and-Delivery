package com.flagcamp.dispatchanddelivery.service;

import com.flagcamp.dispatchanddelivery.entity.HubEntity;
import com.flagcamp.dispatchanddelivery.entity.RobotEntity;
import com.flagcamp.dispatchanddelivery.repository.HubRepository;
import com.flagcamp.dispatchanddelivery.repository.RobotRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * RobotService - 业务逻辑
 *
 * 1. Hub 和 Robot 完整 CRUD
 * 2. 查找最近 Hub
 * 3. 推荐机器人：获取最快 / 最便宜  drone / robot
 */

@Service
public class RobotService {
    private final HubRepository hubRepository;
    private final RobotRepository robotRepository;

    public RobotService(
            HubRepository hubRepository,
            RobotRepository robotRepository) {
        this.hubRepository = hubRepository;
        this.robotRepository = robotRepository;
    }


    // ===================== Hub CRUD =====================
    public HubEntity addHub(double latitude, double longitude, String address) {
        HubEntity hub = new HubEntity(null, latitude, longitude, address);
        return hubRepository.save(hub);
    }

    public List<HubEntity> getAllHubs() {
        return hubRepository.findAll();
    }

    public Optional<HubEntity> getHubById(Long hubId) {
        return hubRepository.findById(hubId);
    }

    public HubEntity updateHub(Long hubId, double latitude, double longitude, String address) {
        HubEntity updated = new HubEntity(hubId, latitude, longitude, address);
        return hubRepository.save(updated);
    }

    public void deleteHub(Long hubId) {
        hubRepository.deleteById(hubId);
    }


    // ===================== Robot CRUD =====================
    public RobotEntity addRobot(boolean available,
                                int battery,
                                Long hubId,
                                double latitude,
                                double longitude,
                                double maxWeight,
                                int speed,
                                double price,
                                String robotType) {
        RobotEntity robot = new RobotEntity(null, available, battery, hubId, latitude, longitude, maxWeight, speed, price, robotType);
        return robotRepository.save(robot);
    }

    public List<RobotEntity> getAllRobots() {
        return (List<RobotEntity>) robotRepository.findAll();
    }

    public Optional<RobotEntity> getRobotById(Long robotId) {
        return robotRepository.findById(robotId);
    }

    public RobotEntity updateRobot(Long robotId,
                                   boolean available,
                                   int battery,
                                   Long hubId,
                                   double latitude,
                                   double longitude,
                                   double maxWeight,
                                   int speed,
                                   double price,
                                   String robotType) {
        RobotEntity updated = new RobotEntity(robotId, available, battery, hubId, latitude, longitude, maxWeight, speed, price, robotType);
        return robotRepository.save(updated);
    }

    public void deleteRobot(Long robotId) {
        robotRepository.deleteById(robotId);
    }


    // ===================== 查找最近 Hub =====================
    public Optional<HubEntity> findNearestHub(double userLat, double userLng) {
        return hubRepository.findAll().stream()
                .min(Comparator.comparingDouble(hub -> distance(hub.latitude(), hub.longitude(), userLat, userLng)));
    }

    // 简单直线距离公式
    private double distance(double lat1, double lng1, double lat2, double lng2) {
        double dx = lat1 - lat2;
        double dy = lng1 - lng2;
        return Math.sqrt(dx * dx + dy * dy);
    }


    // ===================== 推荐机器人 =====================
    // 通用方法
    private Optional<RobotEntity> findBestBy(Long hubId, String type,
                                             Comparator<RobotEntity> comparator) {
        return robotRepository.findAvailableByHubIdAndType(hubId, type)
                .stream()
                .min(comparator); // 或 max，根据比较器调整
    }

    // 找最便宜的
    public Optional<RobotEntity> findCheapestRobot(Long hubId) {
        return findBestBy(hubId, "robot", Comparator.comparingDouble(RobotEntity::price));
    }

    public Optional<RobotEntity> findCheapestDrone(Long hubId) {
        return findBestBy(hubId, "drone", Comparator.comparingDouble(RobotEntity::price));
    }

    // 找最快的（这里 max 用 reverseOrder 或直接调整比较器）
    public Optional<RobotEntity> findFastestRobot(Long hubId) {
        return findBestBy(hubId, "robot", Comparator.comparingInt(RobotEntity::speed).reversed());
    }

    public Optional<RobotEntity> findFastestDrone(Long hubId) {
        return findBestBy(hubId, "drone", Comparator.comparingInt(RobotEntity::speed).reversed());
    }
}
