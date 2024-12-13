package com.service;

import com.model.Admin;

public interface AdminService extends BaseService<Admin, Long> {
    boolean existsByUsername(String username);
}
