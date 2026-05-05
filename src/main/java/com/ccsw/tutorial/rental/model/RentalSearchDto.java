package com.ccsw.tutorial.rental.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

import java.time.LocalDate;

public class RentalSearchDto {
    private PageableRequest pageable;

    private String gameTitle;
    private String customerName;
    private LocalDate date;

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}
