package tempest_double;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entrance of the application
 * */
@SpringBootApplication
public class TempestDoubleApplication {
	/**
	“What’s reality?

	 I don’t know.
	 When my bird was looking at my computer monitor I thought, ‘That bird has no idea what he’s looking at.’
	 And yet what does the bird do? Does he panic? No, he can’t really panic, he just does the best he can.
	 Is he able to live in a world where he’s so ignorant? Well, he doesn’t really have a choice.
	 The bird is okay even though he doesn’t understand the world.
	 You’re that bird looking at the monitor, and you’re thinking to yourself, ‘I can figure this out.’
	 Maybe you have some bird ideas.
	 Maybe that’s the best you can do.”

	 ― Terry A. Davis
	 */
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(TempestDoubleApplication.class, args);
	}

}
