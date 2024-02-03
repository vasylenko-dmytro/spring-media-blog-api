package com.example.controller;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

/**
 * The endpoints you will need can be found in readme.md as well as the test cases.
 * You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should refer to prior mini-project labs and lecture materials for guidance
 * on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
