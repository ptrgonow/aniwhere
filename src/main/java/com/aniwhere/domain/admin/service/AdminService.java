package com.aniwhere.domain.admin.service;

import com.aniwhere.domain.admin.mapper.AdminMapper;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminMapper adminMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public List<JoinDTO> allMembers(){
        return adminMapper.selectAllUsers();
    }

}
