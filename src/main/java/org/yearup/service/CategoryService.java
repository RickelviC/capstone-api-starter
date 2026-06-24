package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getById(int categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category create(Category category) {
        category.setCategoryId(0);

        return categoryRepository.save(category);
    }

    // checks the category to make sure it has been made then changes the name and description of it
    public Category updateExistingCategory(int categoryId, Category category) {
        Category existingCategory = getById(categoryId);

        if (existingCategory == null) return null;
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        return categoryRepository.save(existingCategory);
    }

    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
