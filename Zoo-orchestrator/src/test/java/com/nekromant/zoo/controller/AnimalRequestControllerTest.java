package com.nekromant.zoo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.nekromant.zoo.AnimalRequestUtil;
import dto.AnimalRequestDTO;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AnimalRequestControllerTest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8900);

    private static String appUrl = "http://localhost:8080";//todo взять из конфига, просто накинул шустро

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    @Before
    public void init() {
        wireMockRule.resetMappings();
        wireMockRule.resetScenarios();
        wireMockRule.resetRequests();
    }

    @Test
    public void testSaveUserDetails() throws Exception {
        AnimalRequestDTO request = AnimalRequestUtil.createAnimalRequestDTO();


        wireMockRule.stubFor(WireMock.post(WireMock.urlMatching("/api/smsCode/verify"))
                .willReturn(WireMock.aResponse()
//                        .withBody(expectedResponseString)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));

        // todo   в контроллерах константы по типу /api/animalRequest и /create стоит вынести в константы и использовать тут
        //  чтоб тесты не   ломались от смены мапинга
        ResponseEntity<String> response = restTemplate.postForEntity(appUrl+"/api/animalRequest/create", request, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

}