package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * Created by Pierrot, 13-07-2024.
 */
@Component
@RequiredArgsConstructor
public class BeerHandler {

    public static final String BEER_ID_PATH_VAR = "beerID";
    private final BeerService beerService;

    public Mono<ServerResponse> deleteBeerByID(ServerRequest serverRequest) {
        return beerService.getById(serverRequest.pathVariable(BEER_ID_PATH_VAR))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).flatMap(beerDTO -> beerService
                        .deleteBeerById(beerDTO.getId()))
                .then(ServerResponse.noContent().build());
        }

    public Mono<ServerResponse> patchBeerByID(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService
                        .patchBeer(serverRequest.pathVariable(BEER_ID_PATH_VAR), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateBeerByID(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService
                        .updateBeer(serverRequest.pathVariable(BEER_ID_PATH_VAR), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> createNewBeer(ServerRequest serverRequest) {
        return beerService.saveBeer(serverRequest.bodyToMono(BeerDTO.class))
                .flatMap(beerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH)
                                .build(beerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> getBeerByID(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(beerService.getById(serverRequest.pathVariable(BEER_ID_PATH_VAR)) , BeerDTO.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<ServerResponse> listBeers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }
}
