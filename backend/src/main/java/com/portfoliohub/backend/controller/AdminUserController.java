package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.Entities.User;
import com.portfoliohub.backend.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 1️⃣ Get all users as an admin
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    // 2️⃣ Add a role to a user either keep it user or admin
    @PostMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<String> addRoleToUser(
            @PathVariable Long userId,
            @PathVariable String roleName
    ) {
        adminUserService.addRoleToUser(userId, roleName);
        return ResponseEntity.ok("Role " + roleName + " added to user.");
    }

    // 3️⃣ Remove a role from a user
    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<String> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable String roleName
    ) {
        adminUserService.removeRoleFromUser(userId, roleName);
        return ResponseEntity.ok("Role " + roleName + " removed from user.");
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }
    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enableUser(@PathVariable Long id) {
        adminUserService.setEnabled(id, true);
        return ResponseEntity.ok("User enabled");
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disableUser(@PathVariable Long id) {
        adminUserService.setEnabled(id, false);
        return ResponseEntity.ok("User disabled");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }


}
