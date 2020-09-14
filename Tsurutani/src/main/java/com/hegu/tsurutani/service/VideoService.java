package com.hegu.tsurutani.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface VideoService {

    void add(String msg, String opt, String date, int uId);

    PageInfo<Map<String,Object>> findLogPage(String userno, String optdate, Integer page, Integer limit);

    PageInfo<Map<String,Object>> findCoypPage(Integer page, Integer limit);

    void copy();

    void restore(String backupsno);
}
