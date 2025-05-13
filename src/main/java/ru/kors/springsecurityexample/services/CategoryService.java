package ru.kors.springsecurityexample.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kors.springsecurityexample.models.Categories;
import ru.kors.springsecurityexample.models.Users;
import ru.kors.springsecurityexample.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }

    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Categories> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public void deleteCategoryById(int id) {
        categoryRepository.deleteById(id);
    }
}
