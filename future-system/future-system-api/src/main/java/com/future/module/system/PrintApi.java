package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.feign.utils.FeignName;
import com.future.module.system.fallback.PrintApiFallback;
import com.future.module.system.model.printdev.PrintOption;

import java.util.List;


@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = PrintApiFallback.class, path = "/printDev")
public interface PrintApi {
    @GetMapping("/getListById")
    List<PrintOption> getList(@RequestBody List<String> ids);
}