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

    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return Optional containing the category if found, otherwise empty
     */
    public Optional<Category> getById(int categoryId) {
        return categoryRepository.findById(categoryId);
    }

    /**
     * Creates a new category in the database.
     *
     * @param category the category object to create
     * @return the created category with its generated ID
     */
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Updates an existing category.
     *
     * @param categoryId the ID of the category to update
     * @param updatedCategory the updated category data
     * @return Optional containing the updated category if found, otherwise empty
     */
    public Optional<Category> update(int categoryId, Category updatedCategory) {
        return categoryRepository.findById(categoryId).map(newCategory -> {
            newCategory.setName(updatedCategory.getName());
            newCategory.setDescription(updatedCategory.getDescription());
            return categoryRepository.save(newCategory);
        });
    }

    /**
     * Deletes a category from the database.
     *
     * @param categoryId the ID of the category to delete
     */
    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
