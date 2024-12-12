package doan.ptit.programmingtrainingcenter.specification;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key; // id , name ,...
    private String operation; // = , > , < ,
    private String value;
}
