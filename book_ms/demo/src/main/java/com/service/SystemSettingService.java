package com.service;

import com.model.SystemSetting;
import java.util.List;
import java.util.Optional;

public interface SystemSettingService {
    List<SystemSetting> findAll();
    Optional<SystemSetting> findById(Long id);
    Optional<SystemSetting> findByKey(String key);
    SystemSetting save(SystemSetting setting);
    void deleteById(Long id);
    
    // 获取设置值，如果不存在则返回默认值
    String getSettingValue(String key, String defaultValue);
    int getSettingIntValue(String key, int defaultValue);
    boolean getSettingBooleanValue(String key, boolean defaultValue);
    
    // 更新设置值
    void updateSetting(String key, String value);
    void updateSetting(String key, String value, String description);
    
    // 初始化默认设置
    void initializeDefaultSettings();
} 