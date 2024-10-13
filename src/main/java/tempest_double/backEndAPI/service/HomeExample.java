package tempest_double.backEndAPI.service;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class HomeExample {

    private final List<String> catEmojis = Arrays.asList(
            "/ᐠ - ˕ -マ Ⳋ",
            "ᓚᘏᗢ",
            "ฅ^•ﻌ•^ฅ",
            "(=ಠᆽಠ=)",
            "=＾• ⋏ •＾=",
            "ฅ/ᐠ. ̫ .ᐟ\\ฅ"
    );
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Scheduled(fixedRate = 4000)
    public void updateEmoji() {
        int newIndex = (currentIndex.get() + 1) % catEmojis.size();
        currentIndex.set(newIndex);
    }

    public String getCurrentEmoji() {
        return catEmojis.get(currentIndex.get());
    }
}
