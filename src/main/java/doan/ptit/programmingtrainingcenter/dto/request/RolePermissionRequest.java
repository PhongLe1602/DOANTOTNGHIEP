package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class RolePermissionRequest {
    private String roleId;
    private String permissionId;
}
