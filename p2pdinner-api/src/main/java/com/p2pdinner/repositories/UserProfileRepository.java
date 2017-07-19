/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2pdinner.repositories;

import com.p2pdinner.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rajani
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
	UserProfile findUserProfileByEmailAddress(String emailAddress);
	UserProfile findUserProfileByEmailAddressAndPassword(String emailAddress, String hp);
}
