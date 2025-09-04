package com.order_service.repository;

import com.order_service.entity.Payment;
import com.order_service.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<com.order_service.entity.Payment> findByPreferenceId(String preferenceId);

    Optional<Payment> findByMercadoPagoPaymentId(Long mercadoPagoPaymentId);

    List<Payment> findByUsername(String username);

    List<Payment> findByUsernameAndStatus(String username, PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.externalReference = :externalReference")
    Optional<Payment> findByExternalReference(@Param("externalReference") String externalReference);
}