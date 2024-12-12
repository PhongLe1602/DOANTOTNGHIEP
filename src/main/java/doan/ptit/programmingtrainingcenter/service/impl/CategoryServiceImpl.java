package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.CategoryRequest;
import doan.ptit.programmingtrainingcenter.entity.Category;
import doan.ptit.programmingtrainingcenter.repository.CategoryRepository;
import doan.ptit.programmingtrainingcenter.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setType(categoryRequest.getType());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(String id ,CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setType(categoryRequest.getType());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));
        categoryRepository.delete(category);
    }

}
