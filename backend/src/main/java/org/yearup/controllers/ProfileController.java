package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@PreAuthorize("hasRole('USER')")
@CrossOrigin
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Profile> getProfile(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        return profileService.getProfileByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the profile information for the authenticated user.
     *
     * @param principal the authenticated user making the request
     * @param profile   the updated profile data
     * @return ResponseEntity with status 200 OK and the updated profile
     */
    @PutMapping
    public ResponseEntity<Profile> updateProfile(Principal principal, @RequestBody Profile profile) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        Profile updatedProfile = profileService.updateProfile(userId, profile);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProfile);
    }
}