package com.service.impl;

import com.model.SystemSetting;
import com.repository.SystemSettingRepository;
import com.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    // 默认设置值
    private static final Map<String, String> DEFAULT_SETTINGS = new HashMap<>();
    static {
        DEFAULT_SETTINGS.put(SystemSetting.MAX_BORROWINGS_PER_READER, "5");
        DEFAULT_SETTINGS.put(SystemSetting.DEFAULT_BORROWING_DAYS, "30");
        DEFAULT_SETTINGS.put(SystemSetting.MAX_RENEWALS, "2");
        DEFAULT_SETTINGS.put(SystemSetting.OVERDUE_FINE_PER_DAY, "1.00");
        DEFAULT_SETTINGS.put(SystemSetting.EMAIL_NOTIFICATION_ENABLED, "true");
        DEFAULT_SETTINGS.put(SystemSetting.DUE_REMINDER_DAYS, "3");
    }

    @Override
    public List<SystemSetting> findAll() {
        return systemSettingRepository.findAll();
    }

    @Override
    public Optional<SystemSetting> findById(Long id) {
        return systemSettingRepository.findById(id);
    }

    @Override
    public Optional<SystemSetting> findByKey(String key) {
        return systemSettingRepository.findBySettingKey(key);
    }

    @Override
    public SystemSetting save(SystemSetting setting) {
        return systemSettingRepository.save(setting);
    }

    @Override
    public void deleteById(Long id) {
        systemSettingRepository.deleteById(id);
    }

    @Override
    public String getSettingValue(String key, String defaultValue) {
        return systemSettingRepository.findBySettingKey(key)
                .map(SystemSetting::getSettingValue)
                .orElse(defaultValue);
    }

    @Override
    public int getSettingIntValue(String key, int defaultValue) {
        String value = getSettingValue(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public boolean getSettingBooleanValue(String key, boolean defaultValue) {
        String value = getSettingValue(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    @Override
    @Transactional
    public void updateSetting(String key, String value) {
        updateSetting(key, value, null);
    }

    @Override
    @Transactional
    public void updateSetting(String key, String value, String description) {
        SystemSetting setting = systemSettingRepository.findBySettingKey(key)
                .orElse(new SystemSetting());
        
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        if (description != null) {
            setting.setDescription(description);
        }
        
        systemSettingRepository.save(setting);
    }

    @Override
    @Transactional
    @PostConstruct
    public void initializeDefaultSettings() {
        DEFAULT_SETTINGS.forEach((key, value) -> {
            if (!systemSettingRepository.existsBySettingKey(key)) {
                SystemSetting setting = new SystemSetting();
                setting.setSettingKey(key);
                setting.setSettingValue(value);
                systemSettingRepository.save(setting);
            }
        });
    }
} 