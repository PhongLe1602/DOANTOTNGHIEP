package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {
    private String id;
    private String name;
    private Category.CategoryType type;
    private String description;


}
