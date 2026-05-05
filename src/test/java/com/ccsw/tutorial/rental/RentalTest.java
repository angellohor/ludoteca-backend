package com.ccsw.tutorial.rental;

import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.customer.CustomerService;
import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.rental.model.Rental;
import com.ccsw.tutorial.rental.model.RentalDto;
import com.ccsw.tutorial.rental.model.RentalSearchDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private GameService gameService;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private RentalDto createRentalDto(Long gameId, Long customerId, LocalDate start, LocalDate end) {
        RentalDto dto = new RentalDto();
        GameDto game = new GameDto();
        game.setId(gameId);
        CustomerDto customer = new CustomerDto();
        customer.setId(customerId);

        dto.setGame(game);
        dto.setCustomer(customer);
        dto.setStartDate(start);
        dto.setEndDate(end);
        return dto;
    }

    @Test
    public void saveEndDateBeforeStartDateShouldThrowException() {
        RentalDto rentalDto = createRentalDto(1L, 1L, LocalDate.of(2026, 5, 20), LocalDate.of(2026, 5, 10));

        Exception exception = assertThrows(Exception.class, () -> rentalService.save(null, rentalDto));
        assertEquals("The end date can not be before the start date", exception.getMessage());
        verify(rentalRepository, never()).save(any());
    }

    @Test
    public void savePeriodGreaterThan14DaysShouldThrowException() {
        RentalDto rentalDto = createRentalDto(1L, 1L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 20));

        Exception exception = assertThrows(Exception.class, () -> rentalService.save(null, rentalDto));
        assertEquals("The max rental period is 14 days", exception.getMessage());
        verify(rentalRepository, never()).save(any());
    }

    @Test
    public void saveGameAlreadyRentedShouldThrowException() {
        RentalDto rentalDto = createRentalDto(1L, 1L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 10));

        List<Rental> gameRentals = new ArrayList<>();
        gameRentals.add(new Rental());

        when(rentalRepository.findOverlappingByGame(eq(1L), any(), any(), eq(null))).thenReturn(gameRentals);

        Exception exception = assertThrows(Exception.class, () -> rentalService.save(null, rentalDto));
        assertEquals("This game is already rented", exception.getMessage());
    }

    @Test
    public void saveCustomerWith2RentalsShouldThrowException() {
        RentalDto rentalDto = createRentalDto(1L, 1L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 5));

        when(rentalRepository.findOverlappingByGame(eq(1L), any(), any(), eq(null))).thenReturn(new ArrayList<>());

        List<Rental> customerRentals = new ArrayList<>();
        Rental r1 = new Rental();
        r1.setStartDate(LocalDate.of(2026, 5, 1));
        r1.setEndDate(LocalDate.of(2026, 5, 5));
        Rental r2 = new Rental();
        r2.setStartDate(LocalDate.of(2026, 5, 1));
        r2.setEndDate(LocalDate.of(2026, 5, 5));
        customerRentals.add(r1);
        customerRentals.add(r2);

        when(rentalRepository.findOverlappingByCustomer(eq(1L), any(), any(), eq(null))).thenReturn(customerRentals);

        Exception exception = assertThrows(Exception.class, () -> rentalService.save(null, rentalDto));
        assertEquals("This customer already has 2 rentals", exception.getMessage());
    }

    @Test
    public void saveValidRentalShouldSaveSuccessfully() throws Exception {
        RentalDto rentalDto = createRentalDto(1L, 1L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 10));

        when(rentalRepository.findOverlappingByGame(eq(1L), any(), any(), eq(null))).thenReturn(new ArrayList<>());
        when(rentalRepository.findOverlappingByCustomer(eq(1L), any(), any(), eq(null))).thenReturn(new ArrayList<>());
        when(customerService.get(1L)).thenReturn(new Customer());
        when(gameService.get(1L)).thenReturn(new Game());

        rentalService.save(null, rentalDto);

        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    public void findPageShouldReturnRentalPage() {
        RentalSearchDto searchDto = new RentalSearchDto();
        PageableRequest pageableRequest = new PageableRequest();
        pageableRequest.setPageNumber(0);
        pageableRequest.setPageSize(5);
        searchDto.setPageable(pageableRequest);

        List<Rental> rentals = new ArrayList<>();
        rentals.add(new Rental());
        Page<Rental> expectedPage = new PageImpl<>(rentals);

        when(rentalRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Rental> result = rentalService.findPage(searchDto);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void deleteExistingRentalShouldDelete() throws Exception {
        Rental rental = new Rental();
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.delete(1L);

        verify(rentalRepository).deleteById(1L);
    }

    @Test
    public void deleteNonExistingRentalShouldThrowException() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> rentalService.delete(1L));
        assertEquals("Rental not found", exception.getMessage());
        verify(rentalRepository, never()).deleteById(any());
    }

}
