package com.p2p.data.repositories;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.p2p.domain.DinnerListing;

public interface DinnerListingRepository extends JpaRepository<DinnerListing, Integer> {
    @Query("SELECT DISTINCT d FROM DinnerListing d WHERE d.closeTime > CURRENT_TIMESTAMP AND d.availableQuantity > 0 ")
    Collection<DinnerListing> currentListings();
    
    @Query("SELECT DISTINCT d from DinnerListing d WHERE d.closeTime < CURRENT_TIMESTAMP AND marked = TRUE ")
    Collection<DinnerListing> expiredListings();
    
    Collection<DinnerListing> findByPlaceIdIn(Set<String> placeIds);

    @Query("SELECT DISTINCT d FROM DinnerListing d WHERE d.menuItem.userProfile.id = ? AND d.closeTime > CURRENT_TIMESTAMP AND d.availableQuantity > 0 ")
	Collection<DinnerListing> currentListings(Integer profileId);
    
    @Query("SELECT DISTINCT d FROM DinnerListing d WHERE d.menuItem.userProfile.id = :profileId AND d.startTime BETWEEN :startDate AND :endDate ")
	Collection<DinnerListing> listingsByDate(@Param("profileId") Integer profileId, @Param("startDate") java.sql.Timestamp startDate, @Param("endDate")java.sql.Timestamp endDate);
    
}
