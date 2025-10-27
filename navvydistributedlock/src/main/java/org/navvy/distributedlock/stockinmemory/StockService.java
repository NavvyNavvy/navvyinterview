package org.navvy.distributedlock.stockinmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class StockService {
    private Stock stock = new Stock();

    private Lock lock = new ReentrantLock();

    public void decrease1() {
        stock.setNumber(stock.getNumber() - 1);
        log.info("无锁, 剩余库存: {}",stock.getNumber());
    }

    public synchronized void decrease2() {
        stock.setNumber(stock.getNumber() - 1);
        log.info("sync锁, 剩余库存: {}",stock.getNumber());
    }

    public void decrease3() {
        lock.lock();
        try {
            stock.setNumber(stock.getNumber() - 1);
            log.info("lock锁, 剩余库存: {}",stock.getNumber());
        } finally {
            lock.unlock();
        }
    }


}
