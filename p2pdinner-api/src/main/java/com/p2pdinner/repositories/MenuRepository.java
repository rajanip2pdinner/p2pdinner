package com.p2pdinner.repositories;

import com.p2pdinner.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MenuRepository extends JpaRepository<MenuItem, Integer> {
	@Query("SELECT m from MenuItem m WHERE m.userProfile.id = ?1 AND m.isActive = true")
	List<MenuItem> findMenuByProfileId(Integer profileId);

	@Query("SELECT m from MenuItem m WHERE m.userProfile.id = ?2 AND m.title = ?1 ")
	MenuItem findMenuByTitleAndUserProfileId(String title, Integer userId);

	@Query("DELETE FROM MenuItem m WHERE m.id in :menuItemIds")
	@Modifying
	void deleteMenuItems(@Param("menuItemIds") Set<Integer> menuItemIds);

}
