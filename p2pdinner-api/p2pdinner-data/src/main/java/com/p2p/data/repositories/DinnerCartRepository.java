package com.p2p.data.repositories;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.p2p.domain.vo.OrderedItemDetailVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.p2p.domain.DinnerCart;
import com.p2p.domain.OrderStatus;

@Repository
public interface DinnerCartRepository extends JpaRepository<DinnerCart, Integer> {
	@Query("SELECT d FROM DinnerCart d WHERE d.buyer.id = ?1 AND d.id = ?2 AND status = ?3 ")
	public DinnerCart findByProfileIdAndCartId(Integer profileId, Integer cartId, OrderStatus status) throws Exception;
	
	@Query("SELECT d FROM DinnerCart d WHERE d.buyer.id = ?1 AND status = 'ORDER_IN_PROGRESS' ORDER BY createdDate desc")
	public Page<DinnerCart> findByProfileId(Integer profileId, Pageable pageRequest) throws Exception;

	@Query("SELECT d FROM DinnerCart d INNER JOIN d.buyer u WHERE u.id = :profileId")
	public List<DinnerCart> cartByBuyer(@Param("profileId")Integer profileId);
	

	@Query("SELECT d FROM DinnerCart d INNER JOIN d.buyer u WHERE u.id = :profileId AND d.status = 'RECEIVED'")
	public List<DinnerCart> orderHistory(@Param("profileId")Integer profileId, Pageable pageable);
	
	@Query("SELECT d, ci FROM DinnerCart d INNER JOIN d.cartItems ci WHERE d.status = 'RECEIVED' AND ci.dinnerListing.menuItem.userProfile.id = :profileId")
	public List<Object[]> cartBySeller(@Param("profileId")Integer profileId);

	@Query("SELECT d, ci FROM DinnerCart d INNER JOIN d.cartItems ci WHERE d.status = 'RECEIVED' AND (d.createdDate > :startDate AND d.createdDate <= :endDate)")
	public List<Object[]> findOrdersByDate(@Param("startDate")Calendar startDate, @Param("endDate")Calendar endDate);
	
	@Query("SELECT d, ci FROM DinnerCart d INNER JOIN d.cartItems ci WHERE d.passCode IS NOT NULL AND d.status = 'RECEIVED' AND (d.createdDate > :startDate AND d.createdDate <= :endDate) AND ci.dinnerListing.menuItem.userProfile.tier.tierName = :tierName")
	public List<Object[]> findOrdersByDateAndTier(@Param("startDate")Calendar startDate, @Param("endDate")Calendar endDate, @Param("tierName")String tierName);

	@Query("SELECT d, ci FROM DinnerCart d INNER JOIN d.cartItems ci WHERE d.passCode IS NOT NULL AND d.status = 'RECEIVED' AND (d.createdDate > :startDate AND d.createdDate <= :endDate) AND ci.dinnerListing.menuItem.userProfile.id = :profileId")
	public List<Object[]> findOrdersByProfileAndDate(@Param("profileId")Integer profileId, @Param("startDate")Calendar startDate, @Param("endDate")Calendar endDate);


	@Query("SELECT NEW com.p2p.domain.vo.OrderedItemDetailVO(d.buyer.name, ci.totalPrice, ci.orderQuantity, ci.dinnerListing.menuItem.description, ci.dinnerListing.menuItem.title, d.passCode, ci.totalPrice / ci.orderQuantity ) FROM DinnerCart d INNER JOIN d.cartItems ci WHERE d.passCode IS NOT NULL AND d.status = 'RECEIVED' AND ci.dinnerListing.id = :listingId")
	public List<OrderedItemDetailVO> findReceivedOrderItemDetail(@Param("listingId") Integer listingId);

	@Query("SELECT NEW com.p2p.domain.vo.OrderedItemDetailVO(\n" +
			"  d.buyer.emailAddress, \n" +
			"  ci.totalPrice, \n" +
			"  ci.orderQuantity, \n" +
			"  ci.dinnerListing.menuItem.description, \n" +
			"  ci.dinnerListing.menuItem.title, \n" +
			"  d.passCode, \n" +
			"  ci.totalPrice / ci.orderQuantity, \n" +
			"  ci.dinnerListing.addressLine1, \n" +
			"  ci.dinnerListing.addressLine2, \n" +
			"  ci.dinnerListing.city, \n" +
			"  ci.dinnerListing.state, \n" +
			"  ci.dinnerListing.menuItem.imageUri \n" +
			") FROM DinnerCart d INNER JOIN d.cartItems ci \n" +
			"WHERE d.passCode IS NOT NULL AND \n" +
			"  d.status = 'RECEIVED' AND \n" +
			"  d.buyer.id = :profileId AND\n" +
			"  d.passCode = :passCode")
	public List<OrderedItemDetailVO> findReceivedOrderItemDetailByBuyerAndPassCode(@Param("profileId") Integer profileId, @Param("passCode")String passCode);

	@Query("SELECT NEW com.p2p.domain.vo.OrderedItemDetailVO(\n" +
			"  COALESCE(ci.dinnerListing.menuItem.userProfile.name, ci.dinnerListing.menuItem.userProfile.emailAddress), \n" +
			"  ci.totalPrice, \n" +
			"  ci.orderQuantity, \n" +
			"  ci.dinnerListing.menuItem.description, \n" +
			"  ci.dinnerListing.menuItem.title, \n" +
			"  d.passCode, \n" +
			"  ci.totalPrice / ci.orderQuantity, \n" +
			"  ci.dinnerListing.addressLine1, \n" +
			"  ci.dinnerListing.addressLine2, \n" +
			"  ci.dinnerListing.city, \n" +
			"  ci.dinnerListing.state, \n" +
			"  ci.dinnerListing.startTime, \n" +
			"  ci.dinnerListing.endTime, \n" +
			"  ci.dinnerListing.closeTime, \n" +
			"  ci.dinnerListing.menuItem.imageUri \n" +
			") FROM DinnerCart d INNER JOIN d.cartItems ci \n" +
			"WHERE d.passCode IS NOT NULL AND \n" +
			"  d.status = 'RECEIVED' AND \n" +
			"  d.buyer.id = :profileId AND \n" +
			"  d.createdDate > :startDate  AND \n" +
			"  d.createdDate <= :endDate ")
	public List<OrderedItemDetailVO> findReceivedOrderItemDetailByBuyerAndDate(@Param("profileId")Integer profileId, @Param("startDate")Calendar startDate, @Param("endDate")Calendar endDate);
}
