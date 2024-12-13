-- 先删除旧的外键约束（如果存在）
ALTER TABLE borrowings DROP CONSTRAINT IF EXISTS fk_borrowings_book;

-- 重命名列并修改类型
ALTER TABLE borrowings RENAME COLUMN book_id TO book_isbn;
ALTER TABLE borrowings ALTER COLUMN book_isbn TYPE VARCHAR(255);

-- 添加新的外键约束
ALTER TABLE borrowings 
ADD CONSTRAINT fk_borrowings_book 
FOREIGN KEY (book_isbn) 
REFERENCES books(isbn); 