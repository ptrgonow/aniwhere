package com.aniwhere.domain.admin.service;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import com.aniwhere.domain.admin.mapper.AdminListDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class AdminListDetailService {

    private final AdminListDetailMapper adminListDetailMapper;
    private static final Logger logger = LoggerFactory.getLogger(AdminListDetailService.class);

    @Autowired
    public AdminListDetailService(AdminListDetailMapper adminListDetailMapper) {
        this.adminListDetailMapper = adminListDetailMapper;
    }

    public AdminListDetailDTO getAdminById(String userId) {
        logger.debug("Fetching admin by ID: {}", userId);
        AdminListDetailDTO admin = adminListDetailMapper.findAdminById(userId);
        logger.debug("Admin found: {}", admin);
        return admin;
    }

    public Map<String, Object> getAllAdmins(int limit, int offset, String role) {
        logger.debug("Fetching all admins with limit {} and offset {} for role {}", limit, offset, role);
        List<AdminListDetailDTO> admins = adminListDetailMapper.selectAllAdmins(limit, offset, role);
        int totalAdmins = adminListDetailMapper.countAdmins(role);
        logger.debug("Fetched {} admins, total count: {}", admins.size(), totalAdmins);
        return createResponseMap(admins, totalAdmins, limit, offset);
    }

    public Map<String, Object> searchAdmin(String query, int limit, int offset, String role) {
        logger.debug("Searching admin by query: {} with limit {} and offset {}", query, limit, offset);
        List<AdminListDetailDTO> admins = adminListDetailMapper.searchAdminByQuery(query, limit, offset, role);
        int totalAdmins = adminListDetailMapper.countAdminsByQuery(query, role);
        logger.debug("Found {} admins for query: {}, total count: {}", admins.size(), query, totalAdmins);
        return createResponseMap(admins, totalAdmins, limit, offset);
    }

    private Map<String, Object> createResponseMap(List<AdminListDetailDTO> admins, int totalAdmins, int limit, int offset) {
        int totalPages = (int) Math.ceil((double) totalAdmins / limit);
        int currentPage = offset / limit + 1;

        Map<String, Object> response = new HashMap<>();
        response.put("admins", admins);
        response.put("totalPages", totalPages);
        response.put("currentPage", currentPage);

        logger.debug("Response map: {}", response);

        return response;
    }

    public boolean updateAdmin(AdminListDetailDTO admin) {
        logger.debug("Updating admin: {}", admin);
        return adminListDetailMapper.updateAdmin(admin) == 1;
    }
}



