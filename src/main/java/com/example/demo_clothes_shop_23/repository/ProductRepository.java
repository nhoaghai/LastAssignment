package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Category;
import com.example.demo_clothes_shop_23.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p JOIN FETCH p.colors JOIN FETCH p.sizes")
    List<Product> findAllWithColorsAndSizes();

    @Query("SELECT p FROM Product p JOIN FETCH p.colors ")
    List<Product> findAllWithColors();

    List<Product> findByStatus(boolean status);

    Product findProductByIdAndSlugAndStatus(Integer id, String slug, boolean status);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.category c WHERE c.id = ?1 AND p.status=true AND p.id NOT IN ?2 ORDER BY p.createdAt DESC")
    List<Product> findByCategoryIdOrderByCreatedAtDescExcludingProductId(Integer categoryId, Integer excludedMovieId);

    List<Product> findByStatusOrderByNewPrice(boolean status);

    Page<Product> findAllByStatusOrderByNewPrice(boolean status, Pageable pageable);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status=?1 ORDER BY (p.newPrice - p.price) ASC")
    List<Product> findAllByPriceDifferenceAsc(Boolean status);

    List<Product> findByStatusOrderByCreatedAtDesc(Boolean status);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.category c WHERE c.id = ?1 AND p.status=true")
    List<Product> findByCategoryIdAndStatusTrue(Integer categoryId);

    Page<Product> findByDiscount_IdAndStatus(Integer discount_id, Boolean status, Pageable pageable);

    List<Product> findAllByStatus(Boolean status);

    List<Product> findByDiscount_Id(Integer discount_id);

    List<Product> findByCategoryId(Integer categoryId);

}
