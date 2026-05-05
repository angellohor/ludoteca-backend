package com.ccsw.tutorial.rental;

import com.ccsw.tutorial.rental.model.Rental;
import com.ccsw.tutorial.rental.model.RentalDto;
import com.ccsw.tutorial.rental.model.RentalSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Rental", description = "API of Rental")
@RestController
@RequestMapping(value = "/rental")
@CrossOrigin(origins = "*")
public class RentalController {

    @Autowired
    RentalService rentalService;

    @Autowired
    ModelMapper modelMapper;

    @Operation(summary = "Find All", description = "Method that return all the Rentals")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<RentalDto> findAll() {

        List<Rental> rentals = rentalService.findAll();
        return rentals.stream().map(e -> modelMapper.map(e, RentalDto.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Find Page", description = "Method that return a page of Rentals with filters")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<RentalDto> findPage(@RequestBody RentalSearchDto rentalSearchDto) {

        Page<Rental> page = rentalService.findPage(rentalSearchDto);
        return new PageImpl<>(page.getContent().stream().map(e -> modelMapper.map(e, RentalDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());

    }

    @Operation(summary = "Save or Update", description = "Method that saves or updates a Rental")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody RentalDto rentalDto) throws Exception {
        rentalService.save(id, rentalDto);
    }

    @Operation(summary = "Delete", description = "Method that deletes a Rental")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(name = "id") Long id) throws Exception {
        rentalService.delete(id);
    }
}
