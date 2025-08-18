package com.raved.realtime.controller;

import com.raved.realtime.dto.request.CreateChatRoomRequest;
import com.raved.realtime.dto.response.ChatRoomResponse;
import com.raved.realtime.service.ChatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Test
    void createRoom_returns200() throws Exception {
        Mockito.when(chatService.createChatRoom(Mockito.any(CreateChatRoomRequest.class)))
                .thenReturn(new ChatRoomResponse());

        mockMvc.perform(post("/api/v1/chat/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"General\", \"type\":\"PUBLIC\"}"))
                .andExpect(status().isOk());
    }
}

