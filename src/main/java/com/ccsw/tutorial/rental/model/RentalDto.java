package com.ccsw.tutorial.rental.model;

import com.ccsw.tutorial.customer.model.CustomerDto;
import com.ccsw.tutorial.game.model.GameDto;

import java.time.LocalDate;

public class RentalDto {
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private CustomerDto customer;
    private GameDto game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }
}
