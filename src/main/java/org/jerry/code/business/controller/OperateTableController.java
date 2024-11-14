package org.jerry.code.business.controller;

import lombok.RequiredArgsConstructor;
import org.jerry.code.api.Result;
import org.jerry.code.business.service.IOperateTableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operate")
public class OperateTableController {

    private final IOperateTableService operateTableService;

    @PostMapping("/tables")
    public Result<?> tables(){
        return Result.ok(operateTableService.getTables());
    }
}
