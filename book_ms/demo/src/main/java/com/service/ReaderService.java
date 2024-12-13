package com.service;

import com.model.Reader;
import com.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReaderService {
    List<Reader> findAll();
    Page<Reader> findAll(Pageable pageable);
    Optional<Reader> findById(Long id);
    Optional<Reader> findByUser(User user);
    Reader save(Reader reader);
    void deleteById(Long id);
    boolean existsById(Long id);
}
