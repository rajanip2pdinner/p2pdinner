/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.p2p.domain.UserProfile;

/**
 *
 * @author rajani
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
	UserProfile findUserProfileByEmailAddress(String emailAddress);
	UserProfile findUserProfileByEmailAddressAndPassword(String emailAddress, String hp);
}
