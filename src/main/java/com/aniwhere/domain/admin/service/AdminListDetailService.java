package com.aniwhere.domain.admin.service;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import com.aniwhere.domain.admin.mapper.AdminListDetailMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminListDetailService {

    private final AdminListDetailMapper adminListDetailMapper;

    public List<AdminListDetailDTO> getAllAdmins() {
        return adminListDetailMapper.selectAllAdmins();
    }

    public AdminListDetailDTO getAdminById(String userId) {
        return adminListDetailMapper.selectAdminById(userId);
    }

    public boolean updateAdmin(AdminListDetailDTO admin) {
        return adminListDetailMapper.updateAdmin(admin) > 0;
    }
}
