package com.p2pdinner.repositories;

import com.p2pdinner.domain.DinnerListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

public interface DinnerListingRepository extends JpaRepository<DinnerListing, Integer> {
    @Query("SELECT DISTINCT d FROM DinnerListing d WHERE d.closeTime > :currentTime AND d.availableQuantity > 0 ")
    Collection<DinnerListing> currentListings(@Param("currentTime") java.util.Date currentTime);
    
    @Query("SELECT DISTINCT d from DinnerListing d WHERE d.closeTime < :currentTime AND marked = TRUE ")
    Collection<DinnerListing> expiredListings(@Param("currentTime") java.util.Date currentTime);
    
    Collection<DinnerListing> findByPlaceIdIn(Set<String> placeIds);

    @Query("SELECT DISTINCT d FROM DinnerListing d WHERE d.menuItem.userProfile.id = ? AND d.closeTime > CURRENT_TIMESTAMP AND d.availableQuantity > 0 ")
	Collection<DinnerListing> currentListings(Integer profileId);
    
    @Query("SELECT DISTINCT d FROM DinnerListing d WHERE d.menuItem.userProfile.id = :profileId AND d.startTime BETWEEN :startDate AND :endDate ")
	Collection<DinnerListing> listingsByDate(@Param("profileId") Integer profileId, @Param("startDate") java.util.Date startDate, @Param("endDate") java.util.Date endDate);
    
}
