package com.recipe.service;

import com.recipe.dto.UserDto;
import com.recipe.entity.User;
import com.recipe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserDto getUserProfile(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID không hợp lệ");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + userId));

        return new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getCreatedAt());
    }

   
    @Transactional
    public UserDto updateUserProfile(Long userId, UserDto dto) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID không hợp lệ");
        }
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dữ liệu cập nhật không được rỗng");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + userId));


        if (dto.getFullName() != null && !dto.getFullName().trim().isEmpty()) {
            user.setFullName(dto.getFullName().trim());
        }


        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            String newEmail = dto.getEmail().trim().toLowerCase();
            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng bởi tài khoản khác.");
            }
            user.setEmail(newEmail);
        }


        User saved = userRepository.save(user);
        return new UserDto(saved.getId(), saved.getFullName(), saved.getEmail(), saved.getCreatedAt());
    }
}
