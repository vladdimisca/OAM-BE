package com.oam.repository;

import com.oam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(
        """
            SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
            FROM User u
            WHERE u.userDetails.callingCode = :callingCode AND u.userDetails.phoneNumber = :phoneNumber
        """)
    boolean existsByPhoneNumber(@Param("callingCode") String callingCode, @Param("phoneNumber") String phoneNumber);
}
