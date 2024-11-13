package org.jerry.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/code")
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "index"; // 确保有一个名为 index 的视图文件
    }

    @GetMapping("/generate")
    public String generate() {
        return "generate"; // 确保有一个名为 index 的视图文件
    }

    @GetMapping("/data_source")
    public String dataSource() {
        return "data_source"; // 确保有一个名为 index 的视图文件
    }
}
