package com.manriqueweb.stockcontrol.stockcontrol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
    @Query("SELECT b FROM Product b WHERE b.description like %:des% order by description asc")
	List<Product> retrieveByDescription(@Param("des") String description);
}
