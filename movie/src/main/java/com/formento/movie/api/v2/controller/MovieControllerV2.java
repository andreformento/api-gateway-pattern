package com.formento.movie.api.v2.controller;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/v2")
public class MovieControllerV2 {

    private static final Logger LOG = LoggerFactory.getLogger(MovieControllerV2.class);

    @RequestMapping(value = "/ghostbusters", method = GET)
    public List<String> available() {
        LOG.info(String.format("version 2"));

        return ImmutableList.<String>builder()
                .add("Batman", "Titanic")
                .build();
    }

}
