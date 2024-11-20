package org.jerry.code.business.controller;

import lombok.RequiredArgsConstructor;
import org.jerry.code.api.Result;
import org.jerry.code.business.service.IOperateTableService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operate")
public class OperateTableController {

    private final IOperateTableService operateTableService;

    @PostMapping("/tables")
    public Result<?> tables(){
        return Result.ok(operateTableService.getTables());
    }

    @PostMapping("/tableDDL")
    public Result<?> tableDDL(@RequestParam("tableName") String tableName){
        Result<Object> r = Result.ok();
        String tableDDL = operateTableService.getTableDDL(tableName);
        r.setData(tableDDL);
        return r;
    }


    @DeleteMapping("/removeTable/{tableName}")
    public Result<?> removeTable(@PathVariable String tableName){
        return Result.ok(operateTableService.deleteTable(tableName));
    }
}
