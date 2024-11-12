package org.jerry.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "index"; // 确保有一个名为 index 的视图文件
    }

    @GetMapping("/generate")
    public String generate() {
        return "generate"; // 确保有一个名为 index 的视图文件
    }
}
