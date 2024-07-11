package guru.springframework.reactivemongo.services;

import guru.springframework.reactivemongo.model.BeerDTO;
import reactor.core.publisher.Mono;

public interface BeerService {
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTOMono);

    Mono<BeerDTO> getBeerByID(String beerID);
}
