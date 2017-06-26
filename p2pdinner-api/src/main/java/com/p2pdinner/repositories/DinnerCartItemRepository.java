package com.p2pdinner.repositories;

import com.p2pdinner.domain.DinnerCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DinnerCartItemRepository extends JpaRepository<DinnerCartItem, Integer> {
	@Query("SELECT count(*) FROM DinnerCartItem d where d.dinnerCart.id = ?1")
	public int getCartItemCount(Integer cartId);
	
	@Query("SELECT SUM(i.orderQuantity) FROM DinnerCartItem i INNER JOIN i.dinnerListing dl WHERE dl.id = :listingId AND i.dinnerCart.status = com.p2pdinner.domain.OrderStatus.RECEIVED GROUP BY dl.id ")
	public Integer countOrderQuantityByListingId(@Param("listingId") Integer listingId);
}
