package com.aniwhere.domain.admin.service;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import com.aniwhere.domain.admin.mapper.AdminListDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminListDetailService {

    @Autowired
    private AdminListDetailMapper adminListDetailMapper;

    public List<AdminListDetailDTO> getAllAdmins() {
        return adminListDetailMapper.findAllAdmins();
    }

    public AdminListDetailDTO getAdminDetails(String userId) {
        return adminListDetailMapper.findByUserId(userId);
    }

    public void updateAdmin(AdminListDetailDTO admin) {
        adminListDetailMapper.updateAdmin(admin);
    }
}