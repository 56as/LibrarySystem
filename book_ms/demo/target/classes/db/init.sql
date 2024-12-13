-- 创建数据库
CREATE DATABASE IF NOT EXISTS library DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE library;

-- 创建图书表
CREATE TABLE IF NOT EXISTS books (
    isbn VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    category VARCHAR(50),
    stock INT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入示例数据
INSERT INTO books (isbn, title, author, publisher, category, stock) VALUES
('9787111213826', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', '计算机', 5),
('9787115417305', 'Spring实战', 'Craig Walls', '人民邮电出版社', '计算机', 3),
('9787111563891', '深入理解Java虚拟机', '周志明', '机械工业出版社', '计算机', 4),
('9787115546081', 'Python编程从入门到实践', 'Eric Matthes', '人民邮电出版社', '计算机', 3),
('9787121384561', '算法导论', 'Thomas H.Cormen', '电子工业出版社', '计算机', 2);

-- 创建用户表（如果需要）
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入管理员用户（密码为：admin123）
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt.LAgy', 'ROLE_ADMIN'); 