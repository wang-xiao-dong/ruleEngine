package com.raja.drools.enums;

import java.math.BigDecimal;

/**
 * Created by WXD on 2018/5/18.
 * 电价区间  元/kw
 */
public enum ElectricityPriceRange {
    //谷值区间
    VALLEY_SECOND(0,7, BigDecimal.valueOf(0.5)),
    //峰值区间
    PEAK_ONE(7,24, BigDecimal.valueOf(0.7));
    private Integer start;
    private Integer end;
    private BigDecimal price;

    ElectricityPriceRange(Integer start, Integer end, BigDecimal price) {
        this.start = start;
        this.end = end;
        this.price = price;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public BigDecimal getPrice() {
        return price;
    }
}