package com.yu.controller;

import com.yu.entity.Content;
import com.yu.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class ContentController {

    @Autowired
    ContentService contentService;

    /**
     * 将京东数据存到es中
     */
    @GetMapping("/parseData/{keyword}")
    @ResponseBody
    public boolean parseData(@PathVariable String keyword) throws IOException {
        return contentService.parseData(keyword);
    }


    /**
     * 搜索数据
     */
    @GetMapping("/searchData/{keyword}/{page}/{size}")
    @ResponseBody
    public List<Map<String, Object>> searchData(@PathVariable String keyword, @PathVariable Integer page, @PathVariable Integer size) throws IOException {
        return contentService.searchData(keyword, page, size);
    }

}
