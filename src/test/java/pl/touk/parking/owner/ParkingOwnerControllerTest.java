package pl.touk.parking.owner;

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
import pl.touk.parking.domain.Money;
import pl.touk.parking.web.security.SecurityRoles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ParkingOwnerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ParkingOwnerService service;

    @Autowired
    ApplicationContext context;

    @Before
    public void setUp() {
        when(service.calculateIncomeFor(LocalDate.of(2018, 9, 17)))
                .thenReturn(Money.of(BigDecimal.valueOf(14210.50)));
    }

    @Test
    public void returnsStatusUnauthorizedIfNotAuthenticated() throws Exception {
        mockMvc.perform(get("/income?date=2018-09-17"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    public void returnsStatusForbiddenIfNotAuthenticatedAsOwner() throws Exception {
        mockMvc.perform(get("/income?date=2018-09-17").with(operatorUser()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));
    }

    @Test
    public void returnsStatusOkIfAuthenticatedAsOwner() throws Exception {
        mockMvc.perform(get("/income?date=2018-09-17").with(ownerUser()))
                .andExpect(status().isOk())
                .andExpect(content().string("14210.50"));
    }

    private RequestPostProcessor ownerUser() {
        return user("user").password("pass").roles(SecurityRoles.OWNER);
    }

    private RequestPostProcessor operatorUser() {
        return user("operator").password("pass").roles(SecurityRoles.OPERATOR);
    }
}
