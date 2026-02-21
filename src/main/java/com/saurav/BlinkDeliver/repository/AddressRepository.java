package com.saurav.BlinkDeliver.repository;

import com.saurav.BlinkDeliver.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser_UserId(Long userId);

    List<Address> findAllByUser_UserIdAndIsDefaultTrue(Long userId);
}
