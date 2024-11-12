package org.jerry.code.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.jerry.code.api.Result;
import org.jerry.code.dto.GenerateDTO;
import org.jerry.code.service.ISaveTableService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/table")
public class SaveTableController {

    private final ISaveTableService saveTableService;

    @PostMapping("/save")
    public Result<?> saveTable(String tableName) {
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
}
