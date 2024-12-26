package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CategoryRequest;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.dto.response.SimpleResponse;
import doan.ptit.programmingtrainingcenter.entity.Category;
import doan.ptit.programmingtrainingcenter.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable String id) {
        return categoryService.getCategoryById(id);
    }
    @PostMapping
    Category addCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.addCategory(categoryRequest);
    }
    @PutMapping("/{id}")
    Category updateCategory(@PathVariable String id, @RequestBody CategoryRequest categoryRequest) {
        return categoryService.updateCategory(id, categoryRequest);
    }
    @DeleteMapping("/{id}")
    public SimpleResponse deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return SimpleResponse.success("Xóa thành công danh mục");
    }
    @GetMapping("/all")
    public PagedResponse<Category> getCategories(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) Category.CategoryType type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return categoryService.getCategoriesWithPaginationAndFilters(name, type, page, size);
    }
}
