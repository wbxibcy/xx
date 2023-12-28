-- -- 创建 users 表
-- CREATE TABLE IF NOT EXISTS users (
--     id INTEGER PRIMARY KEY AUTOINCREMENT,
--     username TEXT UNIQUE NOT NULL,
--     account TEXT UNIQUE NOT NULL,
--     hashed_password TEXT NOT NULL,
--     phone_number TEXT,
--     role TEXT
-- );

-- -- 插入一个示例用户
-- INSERT INTO users (username, account, hashed_password, phone_number, role)
-- VALUES ('eva', 'eva02', '$2b$12$qVWUMdBVTo02g3n6KEXldOXa2GgaSuve9Xt1GYL4zcWIvpLXpqI.2', '1234567890', 'user');

-- 创建 users 表
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
);

-- 插入一个示例用户
INSERT INTO users (username, password)
VALUES ('02', '123456');
