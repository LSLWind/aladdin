package org.phoenix.aladdin.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import javax.persistence.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


class HomeControllerTest {

    @Column
    @Test
    void home() {
        HomeController controller=new HomeController();
        MockMvc mockMvc= MockMvcBuilders.standaloneSetup(controller).build();

        try {
            mockMvc.perform(get("/")).andExpect(view().name("home"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}