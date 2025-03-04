package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Cost;
import com.example.demo_clothes_shop_23.request.UpsertCostRequest;
import com.example.demo_clothes_shop_23.service.CostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/costs")
@AllArgsConstructor
public class AdminCostApi {
    private final CostService costService;

    @PutMapping("/create")
    public ResponseEntity<?> createCost(@RequestBody UpsertCostRequest upsertCostRequest) {
        costService.createCost(upsertCostRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{costId}/update")
    public ResponseEntity<?> updateCost(@RequestBody UpsertCostRequest upsertCostRequest, @PathVariable Integer costId) {
        costService.updateCost(upsertCostRequest, costId);
        return ResponseEntity.ok().build();
    }
}
