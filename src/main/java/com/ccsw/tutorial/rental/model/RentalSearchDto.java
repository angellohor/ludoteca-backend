package com.ccsw.tutorial.rental.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

import java.time.LocalDate;

public class RentalSearchDto {
    private PageableRequest pageable;

    private Long gameId;
    private Long customerId;
    private LocalDate date;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
