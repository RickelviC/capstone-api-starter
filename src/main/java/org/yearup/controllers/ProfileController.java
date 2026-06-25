package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")//must be logged in to see the profile
@CrossOrigin
public class ProfileController {
    private ProfileService profileService;
    private UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public Profile getById(Principal principal) {
        int userId = getUser(principal);
        return profileService.getById(userId);
    }

    @PutMapping
    public Profile updateCategory(Principal principal, @RequestBody Profile profile) {
        int userId = getUser(principal);
        Profile updated = profileService.updateById(userId, profile);

        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Users with id " + userId);
        }
        return updated;
    }
    // helper to find database user by username
    private int getUser(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        return user.getId();
    }
}
