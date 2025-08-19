package com.raved.realtime.controller;

import com.raved.realtime.service.WebSocketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WebSocketController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class WebSocketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebSocketService webSocketService;

    @Test
    void sendToRoom_returns202() throws Exception {
        Mockito.doNothing().when(webSocketService).sendToRoom(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        mockMvc.perform(post("/api/v1/ws/rooms/room-1/send")
                        .param("event", "TEST_EVENT")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("payload"))
                .andExpect(status().isAccepted());
    }
}

