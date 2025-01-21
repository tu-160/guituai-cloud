package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.PrintApi;
import com.future.module.system.model.printdev.PrintOption;

import java.util.List;

@Component
public class PrintApiFallback implements PrintApi {
    @Override
    public List<PrintOption> getList(List<String> ids) {
        return null;
    }
}
