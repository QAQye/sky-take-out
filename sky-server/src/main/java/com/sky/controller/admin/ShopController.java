package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关的接口")
public class ShopController {
    @Autowired
    ShopService shopService;

    @ApiOperation("设置营业状态")
    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable("status") Integer status){
        log.info("设置营业状态为{}",status==1?"营业中":"打烊中");
        shopService.setShopStatus(status);
        return Result.success();
    }
    @ApiOperation("查询营业状态")
    @GetMapping("/status")
    public Result<Integer> showShopStatus(){
        log.info("查询营业状态");
        Integer status= shopService.showShopStatus();
        return Result.success(status);
    }



}
