package com.ccsw.tutorial.rental;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.customer.CustomerService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.rental.model.Rental;
import com.ccsw.tutorial.rental.model.RentalDto;
import com.ccsw.tutorial.rental.model.RentalSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    GameService gameService;

    @Override
    public List<Rental> findAll() {
        return (List<Rental>) this.rentalRepository.findAll();
    }

    @Override
    public Page<Rental> findPage(RentalSearchDto rentalSearchDto) {

        Specification<Rental> spec = new RentalSpecification(new SearchCriteria("game.title", ":", rentalSearchDto.getGameTitle()));

        if (rentalSearchDto.getCustomerName() != null) {
            spec = spec.and(new RentalSpecification(new SearchCriteria("customer.name", ":", rentalSearchDto.getCustomerName())));
        }
        if (rentalSearchDto.getDate() != null) {
            spec = spec.and(new RentalSpecification(new SearchCriteria("startDate", "<=", rentalSearchDto.getDate())));
            spec = spec.and(new RentalSpecification(new SearchCriteria("endDate", ">=", rentalSearchDto.getDate())));

        }

        Pageable pageable = rentalSearchDto.getPageable().getPageable();
        return this.rentalRepository.findAll(spec, pageable);
    }

    @Override
    public void save(Long id, RentalDto rentalDto) throws Exception {

        LocalDate start = rentalDto.getStartDate();
        LocalDate end = rentalDto.getEndDate();

        if (end.isBefore(start)) {
            throw new Exception("The end date can not be before the start date");
        }

        long days = ChronoUnit.DAYS.between(start, end);
        if (days > 14) {
            throw new Exception("The max rental period is 14 days");
        }

        List<Rental> gameOverlaps = rentalRepository.findOverlappingByGame(rentalDto.getGame().getId(), start, end, id);
        if (!gameOverlaps.isEmpty()) {
            throw new Exception("This game is already rented");
        }

        List<Rental> customerOverlaps = rentalRepository.findOverlappingByCustomer(rentalDto.getCustomer().getId(), start, end, id);
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            int activeRentals = 0;
            for (Rental overlap : customerOverlaps) {
                if (!date.isBefore(overlap.getStartDate()) && !date.isAfter(overlap.getEndDate())) {
                    activeRentals++;
                }
            }
            if (activeRentals >= 2) {
                throw new Exception("This customer already has 2 rentals");
            }
        }

        Rental rental;
        if (id == null) {
            rental = new Rental();
        } else {
            rental = rentalRepository.findById(id).orElse(null);
        }

        BeanUtils.copyProperties(rentalDto, rental, "id", "customer", "game");

        rental.setCustomer(customerService.get(rentalDto.getCustomer().getId()));
        rental.setGame(gameService.get(rentalDto.getGame().getId()));

        this.rentalRepository.save(rental);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (rentalRepository.findById(id).orElse(null) == null) {
            throw new Exception("Rental not found");
        }
        this.rentalRepository.deleteById(id);
    }
}
