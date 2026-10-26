package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//这里需要指定bean的名称
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺相关的接口")
public class ShopController {
    @Autowired
    ShopService shopService;

    @ApiOperation("查询营业状态")
    @GetMapping("/status")
    public Result<Integer> showShopStatus(){
        log.info("查询营业状态");
        Integer status= shopService.showShopStatus();
        return Result.success(status);
    }
}
