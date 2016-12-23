package com.dk.mp.core.entity;

import java.util.List;

/**
 * 分页实体
 * 作者：janabo on 2016/12/19 10:01
 */
public class PageMsg<T> {
    private int pageSize;//每页多少条
    private int totalPages;//总页数
    private int totalCount;//总条数
    private int nextPage;//下一页
    private int currentPage;//当前页
    private List<T> list;//

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageMsg{" +
                "pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                ", totalCount=" + totalCount +
                ", nextPage=" + nextPage +
                ", currentPage=" + currentPage +
                ", list=" + list +
                '}';
    }
}
