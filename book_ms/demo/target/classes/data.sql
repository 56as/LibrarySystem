-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建测试用户
INSERT INTO users (username, password, email, role, created_at, updated_at)
VALUES 
('admin', '123456', 'admin@example.com', 'ADMIN', NOW(), NOW()),
('reader1', '123456', 'reader1@example.com', 'READER', NOW(), NOW()),
('reader2', '123456', 'reader2@example.com', 'READER', NOW(), NOW());

-- 创建管理员记录
INSERT INTO admins (user_id, name, phone)
SELECT id, '系统管理员', '13800138000'
FROM users
WHERE role = 'ADMIN';

-- 创建对应的读者记录
INSERT INTO readers (user_id, name, phone, address, membership_status)
SELECT id, username, '12345678901', 'HNU', 'ACTIVE'
FROM users
WHERE role = 'READER';

-- 插入计算机类图书数据
INSERT INTO books (isbn, title, author, publisher, category, total_copies, available_copies, created_at, updated_at)
VALUES 
('9787111213826', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', '计算机', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787115417305', 'Spring实战', 'Craig Walls', '人民邮电出版社', '计算机', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787111563891', '深入理解Java虚拟机', '周志明', '机械工业出版社', '计算机', 4, 4, CURRENT_DATE, CURRENT_DATE),
('9787115546081', 'Python编程从入门到实践', 'Eric Matthes', '人民邮电出版社', '计算机', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787121384561', '算法导论', 'Thomas H.Cormen', '电子工业出版社', '计算机', 2, 1, CURRENT_DATE, CURRENT_DATE),
('9787115499851', '深度学习入门：基于Python的理论与实现', '斋藤康毅', '人民邮电出版社', '计算机', 3, 3, CURRENT_DATE, CURRENT_DATE),
('9787115537232', '代码整洁之道', 'Robert C. Martin', '人民邮电出版社', '计算机', 4, 4, CURRENT_DATE, CURRENT_DATE),
('9787111641247', '计算机网络：自顶向下方法', 'James F.Kurose', '机械工业出版社', '计算机', 5, 5, CURRENT_DATE, CURRENT_DATE),
('9787115479228', 'Docker技术入门与实战', '杨保华', '人民邮电出版社', '计算机', 3, 3, CURRENT_DATE, CURRENT_DATE),
('9787115459602', '深入浅出Vue.js', '刘博文', '人民邮电出版社', '计算机', 4, 4, CURRENT_DATE, CURRENT_DATE);

-- 插入文学类图书数据
INSERT INTO books (isbn, title, author, publisher, category, total_copies, available_copies, created_at, updated_at)
VALUES
('9787020002207', '红楼梦', '曹雪芹', '人民文学出版社', '文学', 6, 4, CURRENT_DATE, CURRENT_DATE),
('9787020008735', '百年孤独', '加西亚·马尔克斯', '南海出版公司', '文学', 4, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008742', '活着', '余华', '作家出版社', '文学', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008759', '1984', '乔治·奥威尔', '北京十月文艺出版社', '文学', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008766', '三体', '刘慈欣', '重庆出版社', '文学', 7, 5, CURRENT_DATE, CURRENT_DATE),
('9787020008773', '围城', '钱钟书', '人民文学出版社', '文学', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008780', '平凡的世界', '路遥', '北京十月文艺出版社', '文学', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008797', '白夜行', '东野圭吾', '南海出版公司', '文学', 6, 4, CURRENT_DATE, CURRENT_DATE),
('9787020008803', '追风筝的人', '卡勒德·胡赛尼', '上海人民出版社', '文学', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008810', '解忧杂货店', '东野圭吾', '南海出版公司', '文学', 5, 3, CURRENT_DATE, CURRENT_DATE);

-- 插入历史类图书数据
INSERT INTO books (isbn, title, author, publisher, category, total_copies, available_copies, created_at, updated_at)
VALUES
('9787020008827', '人类简史', '尤瓦尔·赫拉利', '中信出版社', '历史', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008834', '明朝那些事儿', '当年明月', '中国友谊出版公司', '历史', 6, 4, CURRENT_DATE, CURRENT_DATE),
('9787020008841', '万历十五年', '黄仁宇', '中华书局', '历史', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008858', '史记', '司马迁', '中华书局', '历史', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008865', '枪炮、病菌与钢铁', '贾雷德·戴蒙德', '中信出版社', '历史', 4, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008872', '中国历代政治得失', '钱穆', '生活·读书·新知三联书店', '历史', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008889', '全球通史', '斯塔夫里阿诺斯', '北京大学出版社', '历史', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008896', '资治通鉴', '司马光', '中华书局', '历史', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008902', '第二次世界大战战史', '温斯顿·丘吉尔', '商务印书馆', '历史', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008919', '中国大历史', '黄仁宇', '生活·读书·新知三联书店', '历史', 4, 3, CURRENT_DATE, CURRENT_DATE);

-- 插入科学类图书数据
INSERT INTO books (isbn, title, author, publisher, category, total_copies, available_copies, created_at, updated_at)
VALUES
('9787020008926', '时间简史', '史蒂芬·霍金', '湖南科学技术出版社', '科学', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008933', '宇宙', '卡尔·萨根', '上海译文出版社', '科学', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008940', '物种起源', '查尔斯·达尔文', '商务印书馆', '科学', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008957', '费曼物理学讲义', '理查德·费曼', '上海科学技术出版社', '科学', 4, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008964', '从一到无穷大', '乔治·伽莫夫', '科学出版社', '科学', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008971', '上帝掷骰子吗', '曹天元', '北京联合出版公司', '科学', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020008988', '科学革命的结构', '托马斯·库恩', '北京大学出版社', '科学', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020008995', '失控', '凯文·凯利', '新星出版社', '科学', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020009008', '自私的基因', '理查德·道金斯', '中信出版社', '科学', 4, 3, CURRENT_DATE, CURRENT_DATE),
('9787020009015', '果壳中的宇宙', '史蒂芬·霍金', '湖南科学技术出版社', '科学', 5, 4, CURRENT_DATE, CURRENT_DATE);

-- 插入艺术类图书数据
INSERT INTO books (isbn, title, author, publisher, category, total_copies, available_copies, created_at, updated_at)
VALUES
('9787020009022', '艺术的故事', '贡布里希', '广西美术出版社', '艺术', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020009039', '梵高传', '欧文·斯通', '译林出版社', '艺术', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020009046', '中国艺术史', '中央美术学院', '中国青年出版社', '艺术', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020009053', '写给大家看的设计书', '威廉姆斯', '人民邮电出版社', '艺术', 6, 4, CURRENT_DATE, CURRENT_DATE),
('9787020009060', '音乐的故事', '于润洋', '人民音乐出版社', '艺术', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020009077', '中国建筑史', '梁思成', '生活·读书·新知三联书店', '艺术', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020009084', '摄影的艺术', '布鲁斯·巴纳鲍姆', '中国摄影出版社', '艺术', 5, 3, CURRENT_DATE, CURRENT_DATE),
('9787020009091', '电影艺术导论', '戴维·波德维尔', '北京大学出版社', '艺术', 4, 2, CURRENT_DATE, CURRENT_DATE),
('9787020009107', '世界雕塑史', '赫伯特·里德', '三联书店', '艺术', 3, 2, CURRENT_DATE, CURRENT_DATE),
('9787020009114', '中国画的精神', '徐复观', '华东师范大学出版社', '艺术', 4, 3, CURRENT_DATE, CURRENT_DATE);

-- 插入系统设置
INSERT INTO system_settings (setting_key, setting_value, description) 
VALUES 
('max_borrowings_per_reader', '5', '每个读者最大借阅数量'),
('default_borrowing_days', '30', '默认借阅天数'),
('max_renewals', '2', '最大续借次数'),
('overdue_fine_per_day', '1.00', '每天逾期罚金'),
('email_notification_enabled', 'true', '是否启用邮件通知'),
('due_reminder_days', '3', '到期提醒天数'); 