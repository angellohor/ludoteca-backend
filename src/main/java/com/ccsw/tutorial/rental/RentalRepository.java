package com.ccsw.tutorial.rental;

import com.ccsw.tutorial.rental.model.Rental;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends CrudRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {

    @Query("SELECT r FROM Rental r WHERE r.game.id = :idGame AND r.startDate <= :endDate AND r.endDate >= :startDate AND (:idRental IS NULL OR r.id <> :idRental)")
    List<Rental> findOverlappingByGame(@Param("idGame") Long idGame, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("idRental") Long idRental);

    @Query("SELECT r FROM Rental r WHERE r.customer.id = :idCustomer AND r.startDate <= :endDate AND r.endDate >= :startDate AND (:idRental IS NULL OR r.id <> :idRental)")
    List<Rental> findOverlappingByCustomer(@Param("idCustomer") Long idCustomer, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("idRental") Long idRental);

}
