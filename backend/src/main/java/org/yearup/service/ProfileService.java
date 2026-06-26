package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Creates a new user profile in the database.
     *
     * @param profile the profile object to create
     * @return the created profile
     */
    public Profile create(Profile profile) {
        return profileRepository.save(profile);
    }

    /**
     * Retrieves a user profile by their user ID.
     *
     * @param userId the ID of the user
     * @return Optional containing the profile if found, otherwise empty
     */
    public Optional<Profile> getProfileByUserId(int userId) {
        return profileRepository.findById(userId);
    }

    /**
     * Updates an existing user profile with new information.
     * Updates contact and address information.
     *
     * @param userId  the ID of the user whose profile to update
     * @param profile the updated profile data
     * @return the updated profile
     * @throws ResponseStatusException with 404 status if profile not found
     */
    public Profile updateProfile(int userId, Profile profile) {
        Profile existingProfile = profileRepository.findById(userId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existingProfile.setFirstName(profile.getFirstName());
        existingProfile.setLastName(profile.getLastName());
        existingProfile.setPhone(profile.getPhone());
        existingProfile.setEmail(profile.getEmail());
        existingProfile.setAddress(profile.getAddress());
        existingProfile.setCity(profile.getCity());
        existingProfile.setState(profile.getState());
        existingProfile.setZip(profile.getZip());
        return profileRepository.save(existingProfile);
    }
}