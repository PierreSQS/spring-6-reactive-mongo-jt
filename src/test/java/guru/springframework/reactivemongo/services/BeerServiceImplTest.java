package guru.springframework.reactivemongo.services;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.mappers.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    BeerService beerService;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    void saveBeer() {
        Mono<BeerDTO> beerDTOSavedMono = beerService.saveBeer(Mono.just(beerDTO));

        beerDTOSavedMono.subscribe(beerDTO1 -> {
            System.out.println(beerDTO1.getId());
        });
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("33 Export")
                .beerStyle("PILSENER")
                .price(new BigDecimal("12.45" ))
                .upc("12345678")
                .build();
    }
}