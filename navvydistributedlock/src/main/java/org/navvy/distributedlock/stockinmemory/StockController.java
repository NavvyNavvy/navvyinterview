package org.navvy.distributedlock.stockinmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    @Autowired
    private StockService stockService;

    @RequestMapping("decrease1")
    public String decrease1() {
        stockService.decrease1();
        return "ok";
    }

    @RequestMapping("decrease2")
    public String decrease2() {
        stockService.decrease2();
        return "ok";
    }

    @RequestMapping("decrease3")
    public String decrease3() {
        stockService.decrease3();
        return "ok";
    }
}
