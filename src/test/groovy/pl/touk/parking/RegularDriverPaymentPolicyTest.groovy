package pl.touk.parking

import pl.touk.parking.domain.Driver
import pl.touk.parking.domain.Money
import pl.touk.parking.payment.RegularDriverPaymentPolicy
import pl.touk.parking.usage.ParkingUsageService
import spock.lang.Specification

import java.time.Clock
import java.time.LocalTime

import static pl.touk.parking.TimeUtil.at

class RegularDriverPaymentPolicyTest extends Specification {

    ParkingUsageService parkingUsageService

    Clock at16_00 = at(LocalTime.of(16, 0))

    def setup() {
        parkingUsageService = new ParkingUsageService(new InMemoryParkingUsageRepository())
    }

    def 'regular driver pays 1 PLN after the first started hour'() {
        given:
        parkingUsageService.startParkingMeter(regularDriverOfVehicleWith(vehicleNumber()), at16_00)

        when:
        Money payment = parkingUsageService.stopParkingMeter(vehicleNumber(), at(LocalTime.of(16, 59, 59)))

        then:
        payment == Money.of(BigDecimal.ONE)
    }

    def 'regular driver pays 2 PLN after the second started hour'() {
        given:
        parkingUsageService.startParkingMeter(regularDriverOfVehicleWith(vehicleNumber()), at16_00)

        when:
        Money payment = parkingUsageService.stopParkingMeter(vehicleNumber(), at(LocalTime.of(17, 59, 59)))

        then:
        payment == Money.of(BigDecimal.valueOf(2))
    }

    def 'regular driver pays 1.5 times more than the hour before for 3rd and each next hour'() {
        setup:
        parkingUsageService.startParkingMeter(regularDriverOfVehicleWith(vehicleNumber()), at16_00)

        expect:

        parkingUsageService.stopParkingMeter(vehicleNumber(), at(endTime)) == payment
        where:
        endTime                  | payment
        LocalTime.of(18, 0, 0)   | Money.of(2 * 1.5)
        LocalTime.of(18, 59, 59) | Money.of(2 * 1.5)
        LocalTime.of(19, 59, 59) | Money.of(2 * 1.5 * 1.5)
        LocalTime.of(20, 59, 59) | Money.of(2 * 1.5 * 1.5 * 1.5)
    }

    private Driver regularDriverOfVehicleWith(String vehicleNumber) {
        return new Driver(vehicleNumber, new RegularDriverPaymentPolicy())
    }

    private String vehicleNumber() {
        return "KR12345"
    }
}
