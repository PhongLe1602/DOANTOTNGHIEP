package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.CategoryRequest;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.entity.Category;
import doan.ptit.programmingtrainingcenter.repository.CategoryRepository;
import doan.ptit.programmingtrainingcenter.service.CategoryService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public PagedResponse<Category> getCategoriesWithPaginationAndFilters(String name, Category.CategoryType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Specification trực tiếp để lọc theo loại và tìm kiếm theo tên
        Specification<Category> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Start with a true predicate

            // Lọc theo loại
            if (type != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), type));
            }

            // Tìm kiếm theo tên
            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            return predicate;
        };

        Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);

        // Trả về PagedResponse
        return new PagedResponse<>(categoryPage);
    }

}
