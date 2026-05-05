package com.ccsw.tutorial.rental;

import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.customer.model.CustomerDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.rental.model.RentalDto;
import com.ccsw.tutorial.rental.model.RentalSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RentalIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/rental";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<Map<String, Object>> responseTypePage = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    private RentalDto createRentalDto(Long gameId, Long customerId, LocalDate start, LocalDate end) {
        RentalDto rentalDto = new RentalDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(gameId);
        CustomerDto customer = new CustomerDto();
        customer.setId(customerId);

        rentalDto.setGame(gameDto);
        rentalDto.setCustomer(customer);
        rentalDto.setStartDate(start);
        rentalDto.setEndDate(end);
        return rentalDto;
    }

    @Test
    public void findPageWithoutFiltersShouldReturnAllRentals() {
        RentalSearchDto searchDto = new RentalSearchDto();
        PageableRequest pageable = new PageableRequest();
        pageable.setPageNumber(0);
        pageable.setPageSize(5);
        searchDto.setPageable(pageable);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(3, ((Number) response.getBody().get("totalElements")).intValue());
    }

    @Test
    public void findPageWithFilterDateShouldReturnFilteredRentals() {
        RentalSearchDto searchDto = new RentalSearchDto();
        PageableRequest pageable = new PageableRequest();
        pageable.setPageNumber(0);
        pageable.setPageSize(5);
        searchDto.setPageable(pageable);

        searchDto.setDate(LocalDate.of(2026, 5, 12));

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(2, ((Number) response.getBody().get("totalElements")).intValue());
    }

    @Test
    public void saveRentalWithValidInfoShouldCreateNewRental() {
        RentalDto rentalDto = createRentalDto(4L, 3L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 5));

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(rentalDto), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void saveRentalGreater14DaysShouldThrowException() {
        RentalDto rentalDto = createRentalDto(4L, 3L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 20));

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(rentalDto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void saveRentalWhenGameAlreadyRentedShouldThrowException() {
        RentalDto rentalDto = createRentalDto(1L, 3L, LocalDate.of(2026, 5, 12), LocalDate.of(2026, 5, 18));

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(rentalDto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteExistingRentalShouldDeleteRental() {
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/1", HttpMethod.DELETE, null, Void.class);

        RentalSearchDto searchDto = new RentalSearchDto();
        PageableRequest pageable = new PageableRequest();
        pageable.setPageNumber(0);
        pageable.setPageSize(5);
        searchDto.setPageable(pageable);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertEquals(2, ((Number) response.getBody().get("totalElements")).intValue());
    }
}