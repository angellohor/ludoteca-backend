package com.ccsw.tutorial.rental;

import com.ccsw.tutorial.rental.model.Rental;
import com.ccsw.tutorial.rental.model.RentalDto;
import com.ccsw.tutorial.rental.model.RentalSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RentalService {

    List<Rental> findAll();

    Page<Rental> findPage(RentalSearchDto rentalSearchDto);

    void save(Long id, RentalDto rentalDto) throws Exception;

    void delete(Long id) throws Exception;
}
