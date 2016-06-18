package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.p2p.domain.DinnerCartItem;

@Repository
public interface DinnerCartItemRepository extends JpaRepository<DinnerCartItem, Integer> {
	@Query("SELECT count(*) FROM DinnerCartItem d where d.dinnerCart.id = ?1")
	public int getCartItemCount(Integer cartId);
	
	@Query("SELECT SUM(i.orderQuantity) FROM DinnerCartItem i INNER JOIN i.dinnerListing dl WHERE dl.id = :listingId AND i.dinnerCart.status = com.p2p.domain.OrderStatus.RECEIVED GROUP BY dl.id ")
	public Integer countOrderQuantityByListingId(@Param("listingId") Integer listingId);
}
