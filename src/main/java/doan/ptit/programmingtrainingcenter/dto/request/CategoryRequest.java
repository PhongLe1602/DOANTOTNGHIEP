package doan.ptit.programmingtrainingcenter.dto.request;


import doan.ptit.programmingtrainingcenter.entity.Category;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private Category.CategoryType type;
}
