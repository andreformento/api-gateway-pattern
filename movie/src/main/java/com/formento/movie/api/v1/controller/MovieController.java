package com.formento.movie.api.v1.controller;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/v1")
public class MovieController {

    @RequestMapping(value = "/ghostbusters", method = GET)
    public List<String> available() {
        return ImmutableList.<String>builder()
                .add("Ghostbusters (1984)", "Ghostbusters (2016)")
                .build();
    }

}
