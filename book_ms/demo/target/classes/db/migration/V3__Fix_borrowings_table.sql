-- 删除旧表（如果存在）
DROP TABLE IF EXISTS borrowings;

-- 创建新的借阅表
CREATE TABLE borrowings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reader_id BIGINT NOT NULL,
    book_isbn VARCHAR(255) NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) NOT NULL,
    renewal_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reader_id) REFERENCES readers(id),
    FOREIGN KEY (book_isbn) REFERENCES books(isbn)
); 

-- 删除旧的外键约束（如果存在）
ALTER TABLE borrowings DROP CONSTRAINT IF EXISTS fk_borrowings_book;

-- 创建临时列来存储ISBN
ALTER TABLE borrowings ADD COLUMN book_isbn_temp VARCHAR(255);

-- 从books表中获取ISBN并更新临时列
UPDATE borrowings b
JOIN books bk ON b.book_id = bk.id
SET b.book_isbn_temp = bk.isbn;

-- 删除旧列并重命名新列
ALTER TABLE borrowings DROP COLUMN book_id;
ALTER TABLE borrowings RENAME COLUMN book_isbn_temp TO book_isbn;

-- 添加新的外键约束
ALTER TABLE borrowings 
ADD CONSTRAINT fk_borrowings_book 
FOREIGN KEY (book_isbn) 
REFERENCES books(isbn);