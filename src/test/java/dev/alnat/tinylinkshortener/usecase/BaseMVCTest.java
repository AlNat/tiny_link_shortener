package dev.alnat.tinylinkshortener.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.dto.LinkVisitPageResult;
import dev.alnat.tinylinkshortener.dto.VisitOutDTO;
import dev.alnat.tinylinkshortener.dto.common.PaginalResult;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.repository.LinkRepository;
import dev.alnat.tinylinkshortener.repository.VisitRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Some kind of basic test framework for application
 * <p>
 * Created by @author AlNat on 16.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings({"SameParameterValue", "unused", "UnusedReturnValue", "SpringJavaAutowiredMembersInspection"})
public abstract class BaseMVCTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected LinkRepository linkRepository;

    @Autowired
    protected VisitRepository visitRepository;

    protected final ObjectMapper mapper = new ObjectMapper();

    protected static final TypeReference<Result<LinkOutDTO>> LINK_RESPONSE_TYPE = new TypeReference<>() {};


    //////////////////////////////
    // API for link redirection //
    //////////////////////////////


    @SneakyThrows
    protected String redirect(String shortLink, boolean found) {
        if (found) {
            return this.mvc.perform(
                        get("/s/" + shortLink).header("User-Agent", "test")
                    )
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andReturn()
                    .getResponse().getContentAsString(StandardCharsets.UTF_8);
        }

        return this.mvc.perform(get("/s/" + shortLink))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);
    }


    ///////////////////
    // API for links //
    ///////////////////

    @SneakyThrows
    protected Result<LinkOutDTO> getLinkById(Long id) {
        String response = syncResponseToGET("/api/v1/link/" + id);

        try {
             return mapper.readValue(response, LINK_RESPONSE_TYPE);
        } catch (Exception e) {
            Assertions.fail(e);
            return null;
        }
    }

    @SneakyThrows
    protected Result<LinkOutDTO> saveNewLink(LinkInDTO dto) {
        String request;
        try {
            request = mapper.writeValueAsString(dto);
        } catch (RuntimeException e) {
            Assertions.fail(e);
            return null;
        }

        String response = syncResponseToPOST("/api/v1/link/", request);

        try {
            return mapper.readValue(response, LINK_RESPONSE_TYPE);
        } catch (RuntimeException e) {
            Assertions.fail(e);
            return null;
        }
    }

    @SneakyThrows
    protected void deleteLink(Long id) {
        syncResponseToDELETE("/api/v1/link/" + id);
    }

    ////////////////////
    // API for visits //
    ////////////////////


    @SneakyThrows
    protected LinkVisitPageResult getVisits(String shortLink) {
        var requestParams = new LinkedMultiValueMap<String, String>();
        requestParams.add("shortLink", shortLink);

        String response = startGETRequestAndWaitResponse(requestParams, "/api/v1/visit");

        try {
            return mapper.readValue(response, LinkVisitPageResult.class);
        } catch (Exception e) {
            Assertions.fail(e);
            return null;
        }
    }


    ////////////////////////
    // Additional methods //
    ////////////////////////


    @Transactional
    protected void clearDB() {
        visitRepository.deleteAll();;
        linkRepository.deleteAll();
    }


    // API sync call

    @SneakyThrows
    protected String syncResponseToGET(String url) {
        return syncResponseToGET(url, new LinkedMultiValueMap<>());
    }

    @SneakyThrows
    protected String syncResponseToGET(String url, MultiValueMap<String, String> params) {
        return this.mvc.perform(get(url).params(params))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @SneakyThrows
    protected String syncResponseToPOST(String url, String request) {
        return this.mvc.perform(
                        post(url)
                                .content(request)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @SneakyThrows
    protected String syncResponseToDELETE(String url) {
        return this.mvc.perform(delete(url))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);
    }


    // API call with DeferredResult

    @SneakyThrows
    protected String startGETRequestAndWaitResponse(MultiValueMap<String, String> request, String endpoint) {
        MvcResult result = this.mvc.perform(
                get(endpoint)
                        .params(request)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(request().asyncStarted()).andReturn();

        // Await when DeferredResult starts processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Assertions.fail(e);
        }

        // And starts it async dispatch
        result = this.mvc
                .perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        return result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @SneakyThrows
    protected String startPOSTRequestAndWaitResponse(String request, String endpoint) {
        MvcResult result = this.mvc.perform(
                post(endpoint)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(request().asyncStarted()).andReturn();

        // Await when DeferredResult starts processing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Assertions.fail(e);
        }

        // And starts it async dispatch
        result = this.mvc
                .perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        return result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

}
