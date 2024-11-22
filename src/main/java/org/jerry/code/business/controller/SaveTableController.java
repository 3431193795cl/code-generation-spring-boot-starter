package org.jerry.code.business.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.jerry.code.api.Result;
import org.jerry.code.business.dto.GenerateDTO;
import org.jerry.code.business.service.ISaveTableService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/table")
public class SaveTableController {

    private final ISaveTableService saveTableService;

    @PostMapping("/save")
    public Result<?> saveTable(@RequestParam("tableName") String tableName) {
        return Result.ok(saveTableService.saveTable(tableName));
    }

    @PostMapping("generateDML")
    public Result<?> generateDML(@RequestBody GenerateDTO generateDTO) {
        return Result.ok(saveTableService.generateDML(generateDTO));
    }

    @PostMapping("generateDDL")
    public Result<?> generateDDL(@RequestBody GenerateDTO generateDTO) {
        JSONObject json = new JSONObject();
        json.put("ddl", saveTableService.generateDDL(generateDTO));
        return Result.ok(json);
    }

    @PostMapping("parsingSql")
    public Result<?> parsingSql(@RequestBody String tableName) {
        return Result.ok(saveTableService.parsingSql(tableName));
    }

    @PostMapping("getPackageInfo")
    public Result<?> getPackageInfo() {
        return Result.ok(saveTableService.getPackageInfo());
    }
}
