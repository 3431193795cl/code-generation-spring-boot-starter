package org.jerry.code.controller;

import lombok.RequiredArgsConstructor;
import org.jerry.code.api.Result;
import org.jerry.code.service.ISaveTableService;
import org.springframework.web.bind.annotation.PostMapping;
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
}
