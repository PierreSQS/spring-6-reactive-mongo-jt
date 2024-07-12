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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

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
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        Mono<BeerDTO> beerDTOSavedMono = beerService.saveBeer(Mono.just(beerDTO));

        beerDTOSavedMono.subscribe(savedDto -> {
            System.out.println("The saved BeerID: "+savedDto.getId());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);


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