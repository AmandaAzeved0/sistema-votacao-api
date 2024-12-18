package org.sicredi.external;

import lombok.RequiredArgsConstructor;
import org.sicredi.model.dto.RandomUserResponse;
import org.sicredi.model.dto.RandomUserResponse.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class RandomUserClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://randomuser.me/api/")
            .build();

    public  Result fetchRandomUser() {
        RandomUserResponse response = webClient.get()
                .retrieve()
                .bodyToMono(RandomUserResponse.class)
                .block();

        return response.getResults().get(0);
    }
}
