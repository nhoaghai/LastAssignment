package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Size;
import com.example.demo_clothes_shop_23.model.enums.SizeType;
import com.example.demo_clothes_shop_23.repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizeService {
    private final SizeRepository sizeRepository;

    public Set<Size> getAll() {
        return new HashSet<>(sizeRepository.findAll());
    }
    public Set<Size> findSizeByTypeOrderByOrdersAsc(SizeType type){
        return sizeRepository.findSizeByTypeOrderByOrdersAsc(type);
    }
}
