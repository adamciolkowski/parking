package pl.touk.parking;

public class IdGenerator {

    private Long id = 1L;

    public Long nextId() {
        return id++;
    }
}
