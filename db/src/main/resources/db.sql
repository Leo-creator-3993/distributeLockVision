CREATE TABLE distributed_lock (
    lock_name VARCHAR(64) PRIMARY KEY,      -- 锁的名称，唯一标识
    lock_owner VARCHAR(64),                 -- 锁的持有者标识
    expires_at TIMESTAMP                    -- 锁的过期时间
);