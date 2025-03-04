package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Size;
import com.example.demo_clothes_shop_23.model.enums.SizeType;
import com.example.demo_clothes_shop_23.service.SizeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sizes")
@AllArgsConstructor
public class AdminSizeApi {
    private final SizeService sizeService;

    @GetMapping("/sizeType/{sizeType}")
    public ResponseEntity<?> getSizeBySizeType(@PathVariable("sizeType") String sizeType) {
        List<Size> sizes = sizeService.findSizeByTypeOrderByOrdersAsc(SizeType.valueOf(sizeType)).stream().toList();
        return ResponseEntity.ok(sizes);
    }

}
