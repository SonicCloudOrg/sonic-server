package com.sonic.controller.models.interfaces;

/**
 * @author ZhouYiXun
 * @des 定义状态
 * @date 2021/8/15 19:31
 */
public interface BugsStatus {
    int NO_FIX = 1;//未修复
    int FIXED = 2;//已修复
    int CANNOT_FIX = 3;//无法修复
}
