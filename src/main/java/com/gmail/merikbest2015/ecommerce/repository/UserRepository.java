package com.gmail.merikbest2015.ecommerce.repository;

import com.gmail.merikbest2015.ecommerce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "roles")
    User findByPhoneNumber(String phoneNumber);

//    @EntityGraph(attributePaths = "roles")
//    User findByActivationCode(String code);

//    @Query("SELECT user.phoneNumber FROM User user WHERE user.passwordResetCode = :code")
//    Optional<String> getPhoneNumberByPasswordResetCode(String code);

    @EntityGraph(attributePaths = "roles")
    @Query("SELECT user FROM User user WHERE " +
            "(CASE " +
            "   WHEN :searchType = 'fullName' THEN UPPER(user.fullName) " +
            "   WHEN :searchType = 'phoneNumber' THEN user.phoneNumber " +
            "END) " +
            "LIKE UPPER(CONCAT('%',:text,'%'))")
    Page<User> searchUsers(String searchType, String text, Pageable pageable);
}
