package springresourceserver.config;

import no.maskinporten.example.springresourceserver.SpringResourceServerApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
                    classes = SpringResourceServerApplication.class)
@AutoConfigureMockMvc
public class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAccessToPublicEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/public"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAccessDeniedToSecuredEndpointForAnonymousMockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
        .get("/hello"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void getAccessToSecuredEndpointForMockUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .post("/hello")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_myprefix:myscope"))))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals(response, "Hello consumer with identifier null");
    }
}
