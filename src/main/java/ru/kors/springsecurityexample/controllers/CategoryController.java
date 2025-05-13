package ru.kors.springsecurityexample.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kors.springsecurityexample.models.Categories;
import ru.kors.springsecurityexample.services.CategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("category")
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;

    @PostMapping("/add-category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Categories addCategory(@RequestBody Categories category) {
        return categoryService.addCategory(category);
    }

    @GetMapping("/get-all")
    public List<Categories> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Categories> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Integer id) {
        categoryService.deleteCategoryById(id);
    }
}
