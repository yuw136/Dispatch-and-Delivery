package com.flagcamp.dispatchanddelivery.controller;

import com.flagcamp.dispatchanddelivery.model.OrderRequestDTO;
import com.flagcamp.dispatchanddelivery.model.OrderResponseDTO;
import com.flagcamp.dispatchanddelivery.model.PositionResponse;
import com.flagcamp.dispatchanddelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. GET /dashboard/orders -> Return list of orders for the current user
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrderList() {
        // Hardcoded user ID "1" for now, should be retrieved from SecurityContext in production
        return ResponseEntity.ok(orderService.getOrderList("user-bob"));
    }

    // 2. POST /dashboard/orders/preview -> Preview delivery options
    @PostMapping("/preview")
    public ResponseEntity<Object> previewOptions(@RequestBody Map<String, Object> payload) {
        Double fromLat = ((Number) payload.get("from_lat")).doubleValue();
        Double fromLng = ((Number) payload.get("from_lng")).doubleValue();
        Double toLat = ((Number) payload.get("to_lat")).doubleValue();
        Double toLng = ((Number) payload.get("to_lng")).doubleValue();

        return ResponseEntity.ok(orderService.previewOptions(fromLat, fromLng, toLat, toLng));
    }

    // 3. POST /dashboard/orders/submit -> Submit order
    @PostMapping("/submit")
    public ResponseEntity<Object> submitOrder(@RequestBody Map<String, Object> payload) {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setFromAddress((String) payload.get("from_address"));
        dto.setToAddress((String) payload.get("to_address"));
        dto.setDuration((Integer) payload.get("duration"));
        dto.setPrice(((Number) payload.get("price")).doubleValue());
        dto.setItemDescription((String) payload.get("item_description"));
        dto.setWeight(((Number) payload.get("weight")).doubleValue());
        dto.setRobotType((String) payload.get("robot_type"));
        dto.setRobotId((String) payload.get("robot_id"));
        dto.setRoute((String) payload.get("route"));
        dto.setDistance(((Number) payload.get("distance")).longValue());

        // Extract lat/lng coordinates from payload
        dto.setFromLat(((Number) payload.get("fromLat")).doubleValue());
        dto.setFromLng(((Number) payload.get("fromLng")).doubleValue());
        dto.setToLat(((Number) payload.get("toLat")).doubleValue());
        dto.setToLng(((Number) payload.get("toLng")).doubleValue());

        // TODO: Get userId from SecurityContext in production
        String userId = "1"; // Hardcoded for now
        Map<String, Object> result = orderService.submitOrder(userId, dto);
        return ResponseEntity.ok(result);
    }

    // 4. GET /dashboard/orders/tracking?order_id=...
    @GetMapping("/tracking")
    public ResponseEntity<PositionResponse> getTracking(@RequestParam("order_id") String orderId) {
        return ResponseEntity.ok(orderService.getTrackingData(orderId));
    }
}