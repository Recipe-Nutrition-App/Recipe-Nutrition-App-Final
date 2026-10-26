package com.recipe.controller;

import com.recipe.dto.UserDto;
import com.recipe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    // Tiêm (Inject) UserService thay vì tiêm trực tiếp UserRepository
    public UserController(UserService userService) {
        this.userService = userService;
    }

    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestParam(required = false) Long userId,
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {

        Long targetId = userId != null ? userId : (headerUserId != null ? headerUserId : 1L);
        UserDto dto = userService.getUserProfile(targetId);
        return ResponseEntity.ok(dto);
    }

    
    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(
            @RequestParam(required = false) Long userId,
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @RequestBody UserDto dto) {

        Long targetId = userId != null ? userId : (headerUserId != null ? headerUserId : 1L);
        UserDto updatedDto = userService.updateUserProfile(targetId, dto);
        return ResponseEntity.ok(updatedDto);
    }
}
