## 工程说明
```
本工程聚焦于研究分布式锁,包括基于redis、zookeeper等的分布式锁的设计与实现
1. 基于redis 分布式锁的设计与应用
2. 基于zookeeper 分布式锁的设计与应用
3. 基于数据库(mysql) 分布式锁的设计与应用
```

## 分布式锁与普通锁
```
普通锁用于在单个进程内解决多线程的并发问题吗,而分布式锁则用于在多进程、多服务器或分布式环境中实现共享资源的并发控制
普通锁一般依赖于进程内的机制（如 `synchronized` 或 `ReentrantLock`）,只在本地进程内有效;而分布式锁需要考虑跨进程通信、锁失效处理、网络分区等因素，来保证在分布式系统中的一致性和可靠性
分布式锁不仅可以解决普通锁的问题,还能扩展到跨服务器的分布式环境下,确保各节点在竞争访问资源时保持互斥性.因此，可以将普通锁视为分布式锁的特例,但分布式锁的实现需要更复杂的分布式系统支持
```