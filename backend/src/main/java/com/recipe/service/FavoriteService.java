package com.recipe.service;

import com.recipe.dto.FavoriteRecipeDto;
import com.recipe.entity.Favorite;
import com.recipe.entity.Recipe;
import com.recipe.entity.User;
import com.recipe.repository.FavoriteRepository;
import com.recipe.repository.RecipeRepository;
import com.recipe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           RecipeRepository recipeRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoriteRecipeDto> getUserFavorites(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID không hợp lệ");
        }

        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream()
                .map(fav -> FavoriteRecipeDto.from(fav.getRecipe()))
                .toList();
    }


    @Transactional
    public boolean addFavorite(Long recipeId, Long userId) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID không hợp lệ");
        }
        if (recipeId == null || recipeId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe ID không hợp lệ");
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + userId));


        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy món ăn với ID: " + recipeId));

        if (favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId)) {
            return false; // Đã có sẵn trong danh sách
        }


        favoriteRepository.save(new Favorite(user, recipe));
        return true; // Thêm mới thành công
    }


    @Transactional
    public void removeFavorite(Long recipeId, Long userId) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID không hợp lệ");
        }
        if (recipeId == null || recipeId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe ID không hợp lệ");
        }


        Favorite favorite = favoriteRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Món ăn này chưa có trong danh sách yêu thích"));


        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Long recipeId, Long userId) {
        if (userId == null || userId <= 0 || recipeId == null || recipeId <= 0) {
            return false;
        }
        return favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId);
    }
}
