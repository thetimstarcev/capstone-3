package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(int categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> update(int categoryId, Category updatedCategory) {
        return categoryRepository.findById(categoryId).map(newCategory -> {
            newCategory.setName(updatedCategory.getName());
            newCategory.setDescription(updatedCategory.getDescription());
            return categoryRepository.save(newCategory);
        });
    }

    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
