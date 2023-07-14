package com.hackthon.voiceProcessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSuggestionRepository extends JpaRepository<ProductSuggestionEntity, List<String>>{

	@Query(value = "SELECT product_id FROM product_suggestion_library WHERE keyword IN (:entities)" , nativeQuery = true)
	public List<String> getSuggestedProductIds(@Param("entities") List<String> entities);
}
