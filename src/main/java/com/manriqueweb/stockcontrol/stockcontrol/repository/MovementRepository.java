package com.manriqueweb.stockcontrol.stockcontrol.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.manriqueweb.stockcontrol.stockcontrol.entity.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {

	@Query(value = "SELECT a.id, a.concept_type, a.date_movement, a.quantity, a.product_id FROM Movement a WHERE (a.date_movement >= :fromDate and a.date_movement <= :toDate) order by date_movement asc", 
	  nativeQuery = true)
    List<Movement> retrieveBetweenDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

}
