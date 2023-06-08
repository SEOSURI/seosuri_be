package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.classification.CategoryResDto;

public class CategoryCount implements Comparable{
    private int count = 0;
    private boolean isVIP = false;
    private CategoryResDto categoryResDto;
    CategoryCount(int count, boolean isVIP, CategoryResDto categoryResDto){
        this.count = count;
        this.isVIP = isVIP;
        this.categoryResDto = categoryResDto;
    }

    @Override
    public int compareTo(Object o) {
        CategoryCount e = (CategoryCount) o;
        if(getCount() > e.getCount()){
            return -1;
        } else if(getCount() < e.getCount()){
            return 1;
        } else{
            if(getIsVIP()){
                return -1;
            }
            if(e.getIsVIP()){
                return 1;
            }
            return 0;
        }
        //return getCount() - e.getCount();
    }

    public int getCount() {
        return count;
    }

    public boolean getIsVIP() {
        return isVIP;
    }

    public CategoryResDto getCategoryResDto() {
        return categoryResDto;
    }
}

