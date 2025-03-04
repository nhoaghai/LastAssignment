package com.example.demo_clothes_shop_23.specifications;

import com.example.demo_clothes_shop_23.entities.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecifications {

    public static Specification<Product> findProducts(
        Integer sizeId,
        Integer colorId,
        Boolean status,
        String nameKeyword,
        Integer categoryParentId,
        Integer categoryChildId,
        String sortProduct,
        Double startPrice,
        Double endPrice
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (sizeId != null) {
                predicates.add(cb.equal(root.join("sizes").get("id"), sizeId));
            }

            if (colorId != null) {
                predicates.add(cb.equal(root.join("colors").get("id"), colorId));
            }

            if (categoryChildId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryChildId));
            }

            if (categoryParentId != null) {
                predicates.add(cb.equal(root.get("category").get("parentId"), categoryParentId));
            }

            if (nameKeyword != null && !nameKeyword.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + nameKeyword + "%"));
            }

            if (startPrice != null && endPrice != null) {
                predicates.add(cb.between(root.get("newPrice"), startPrice, endPrice));
            } else if (startPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("newPrice"), startPrice));
            } else if (endPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("newPrice"), endPrice));
            }

            if ("asc".equalsIgnoreCase(sortProduct)) {
                query.orderBy(cb.asc(root.get("newPrice")));
            } else if ("desc".equalsIgnoreCase(sortProduct)) {
                query.orderBy(cb.desc(root.get("newPrice")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
