package com.controller;

import com.model.Reader;
import com.service.ReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/readers", produces = "text/html;charset=UTF-8")
public class ReaderController {

    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

    @Autowired
    private ReaderService readerService;

    @GetMapping
    public String listReaders(Model model) {
        logger.info("Accessing reader list page");
        try {
            List<Reader> readers = readerService.findAll();
            logger.debug("Retrieved {} readers from database", readers != null ? readers.size() : 0);
            
            if (readers == null) {
                logger.error("Reader service returned null list");
                model.addAttribute("error", "系统错误：无法获取读者列表");
                return "reader/list";
            }

            // 过滤掉没有关联用户的读者
            List<Reader> validReaders = readers.stream()
                .filter(reader -> {
                    if (reader.getUser() == null) {
                        logger.error("Reader with ID {} has no associated User - excluding from list", reader.getId());
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

            if (validReaders.isEmpty()) {
                if (readers.isEmpty()) {
                    logger.info("No readers found in database");
                    model.addAttribute("info", "暂无读者数据");
                } else {
                    logger.warn("Found {} readers but all have invalid User associations", readers.size());
                    model.addAttribute("warning", "所有读者数据均存在问题，请检查用户关联");
                }
            } else {
                logger.info("Successfully retrieved {} valid readers out of {} total readers", 
                           validReaders.size(), readers.size());
                if (validReaders.size() < readers.size()) {
                    model.addAttribute("warning", String.format("发现 %d 条无效的读者数据被过滤", 
                                     readers.size() - validReaders.size()));
                }
            }

            model.addAttribute("readers", validReaders);

        } catch (Exception e) {
            logger.error("Error retrieving readers list: ", e);
            model.addAttribute("error", "获取读者列表失败: " + e.getMessage());
        }

        model.addAttribute("pageTitle", "读者列表 - 图书管理系统");
        return "reader/list";
    }
}
