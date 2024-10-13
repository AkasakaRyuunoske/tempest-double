package tempest_double.backEndAPI.service;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class HomeExample {

    private final AtomicInteger currentValue = new AtomicInteger(0);
    private final Random random = new Random();

    @Scheduled(fixedRate = 100)
    public void updateValue() {
        int randomFactor = random.nextInt(10) - 5;
        int newValue = currentValue.get() + randomFactor;
        newValue = Math.max(0, Math.min(100, newValue));
        currentValue.set(newValue);
    }

    public int getCurrentValue() {
        return currentValue.get();
    }
}
