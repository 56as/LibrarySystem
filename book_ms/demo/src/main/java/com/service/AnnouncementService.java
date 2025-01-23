package com.service;

import com.model.Announcement;
import java.util.List;

public interface AnnouncementService {
    List<Announcement> findAll();
    Announcement save(Announcement announcement);
    void deleteById(Long id);
}
