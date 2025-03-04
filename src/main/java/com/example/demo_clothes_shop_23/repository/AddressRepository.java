package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    public List<Address> findByUser_Id(Integer userId);

    public Address findByUser_IdAndChosen(Integer user_id, Boolean chosen);
}
