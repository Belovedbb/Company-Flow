package com.company.go.global;

import com.company.go.ExcludePortConfig;
import com.company.go.application.port.in.global.DashBoardUseCase;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringBootConfiguration.class, ExcludePortConfig.class})
@WebMvcTest(controllers = GlobalController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class GlobalControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndexUseCase indexer;

    @MockBean
    private DashBoardUseCase dashBoardUseCase;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;


    @Test
    @DisplayName("Setup global login")
    public void setUpGlobal() throws Exception {
        mockMvc.perform(get("/login")
                .contentType("application/html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login / SignUp")));
    }

    @Test
    @DisplayName("Setup global index")
    public void setUpIndex() throws Exception {
        when(dashBoardUseCase.getAllSummaries()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/")
                .contentType("application/html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Company Flow")));
    }

    @Test
    @DisplayName("Setup global register")
    public void setUpRegister() throws Exception {
        mockMvc.perform(get("/register")
                .contentType("application/html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Registration")));
    }


    @ParameterizedTest
    @MethodSource("provideRegisterViewModel")
    @DisplayName("Test global save login register")
    public void globalFilteredListing(RegisterUserUseCase.RegisterUserModel model) throws Exception {
        mockMvc.perform(
                post("/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(model))
        ).andExpect(status().isOk());
    }

    private static Stream<Arguments> provideRegisterViewModel() {
        RegisterUserUseCase.RegisterUserModel registerUserModel = new RegisterUserUseCase.RegisterUserModel();
        registerUserModel.setFirstName("");
        registerUserModel.setLastName("");
        registerUserModel.setEmail("");
        registerUserModel.setPassword("");
        registerUserModel.setAlias("");
        registerUserModel.setPhoneNumber("");
        registerUserModel.setAddress("");
        registerUserModel.setPictureData("Picture");
        registerUserModel.setPictureType("image/png");
        return Stream.of(
                Arguments.of(registerUserModel)
        );
    }
}
