package pl.touk.parking.usage;

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
import pl.touk.parking.domain.Driver;
import pl.touk.parking.domain.Money;
import pl.touk.parking.payment.RegularDriverPaymentPolicy;

import java.math.BigDecimal;
import java.time.Clock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ParkingUsageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ParkingUsageService service;

    @Autowired
    ApplicationContext context;

    @Before
    public void setUp() {
        when(service.stopParkingMeter("KR12345", Clock.systemDefaultZone()))
                .thenReturn(Money.of(BigDecimal.valueOf(2.50)));
    }

    @Test
    public void returnsStatusOkIfNotAuthenticated() throws Exception {
        mockMvc.perform(get("/start?vehicleNumber=KR12345&driverType=REGULAR"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        verify(service).startParkingMeter(
                new Driver("KR12345", new RegularDriverPaymentPolicy()),
                Clock.systemDefaultZone()
        );
    }

    @Test
    public void returnsStatusOkAndPaymentWhenParkingMeterIsStopped() throws Exception {
        mockMvc.perform(get("/stop?vehicleNumber=KR12345&driverType=REGULAR"))
                .andExpect(status().isOk())
                .andExpect(content().string("2.50"));
    }
}
