package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Cost;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.CostRepository;
import com.example.demo_clothes_shop_23.request.UpsertCostRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CostService {
    private final CostRepository costRepository;
    private final UserService userService;

    public Long getCostsCreatedThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        return costRepository.findCostsCreatedThisMonth(currentMonth, currentYear);
    }

    public Cost getById(int id) {
        return costRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No cost found with id " + id)
        );
    }

    public List<Cost> getAll() {
        return costRepository.findAll();
    }

    public void createCost(UpsertCostRequest upsertCostRequest) {
        User user = userService.getById(upsertCostRequest.getUser());
        Cost cost = Cost.builder()
            .name(upsertCostRequest.getName())
            .amount(upsertCostRequest.getAmount())
            .description(upsertCostRequest.getDescription())
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        costRepository.save(cost);
    }

    public void updateCost(UpsertCostRequest upsertCostRequest, Integer costId) {
        User user = userService.getById(upsertCostRequest.getUser());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = (User) userDetails.getUser();
        if(!Objects.equals(currentUser.getId(), user.getId())){
            throw new RuntimeException("Bạn không được phép cập nhật chi phí này");
        }
        Cost cost = costRepository.findById(costId).orElseThrow(
            () -> new ResourceNotFoundException("No cost found with id " + costId)
        );
        cost.setName(upsertCostRequest.getName());
        cost.setAmount(upsertCostRequest.getAmount());
        cost.setDescription(upsertCostRequest.getDescription());
        cost.setUser(user);
        cost.setUpdatedAt(LocalDateTime.now());
        costRepository.save(cost);
    }
}
