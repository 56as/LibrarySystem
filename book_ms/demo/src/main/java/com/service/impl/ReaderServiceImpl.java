package com.service.impl;

import com.model.Reader;
import com.model.User;
import com.repository.ReaderRepository;
import com.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    @Override
    public Page<Reader> findAll(Pageable pageable) {
        return readerRepository.findAll(pageable);
    }

    @Override
    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }

    @Override
    public Reader save(Reader reader) {
        return readerRepository.save(reader);
    }

    @Override
    public void deleteById(Long id) {
        readerRepository.deleteById(id);
    }

    @Override
    public Optional<Reader> findByUser(User user) {
        return readerRepository.findByUser(user);
    }

    @Override
    public boolean existsById(Long id) {
        return readerRepository.existsById(id);
    }
}
