package pl.touk.parking.operations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import pl.touk.parking.web.security.SecurityRoles;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ParkingOperatorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ParkingOperatorService service;

    @Autowired
    ApplicationContext context;

    @Before
    public void setUp() {
        when(service.hasStartedParkingMeter("KR12345")).thenReturn(true);
    }

    @Test
    public void returnsStatusUnauthorizedIfNotAuthenticated() throws Exception {
        mockMvc.perform(get("/check?vehicleNumber=KR12345"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    public void returnsStatusForbiddenIfNotAuthenticatedAsOperator() throws Exception {
        mockMvc.perform(get("/check?vehicleNumber=KR12345").with(ownerUser()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));
    }

    @Test
    public void returnsStatusOkIfAuthenticatedAsOperator() throws Exception {
        mockMvc.perform(get("/check?vehicleNumber=KR12345").with(operatorUser()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    private RequestPostProcessor ownerUser() {
        return user("user").password("pass").roles(SecurityRoles.OWNER);
    }

    private RequestPostProcessor operatorUser() {
        return user("operator").password("pass").roles(SecurityRoles.OPERATOR);
    }
}
