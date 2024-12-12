package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.CategoryRequest;
import doan.ptit.programmingtrainingcenter.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Category getCategoryById(String id);
    List<Category> getAllCategories();
    Category addCategory(CategoryRequest categoryRequest);
    Category updateCategory(String id ,CategoryRequest categoryRequest);
    void deleteCategory(String id);
}
