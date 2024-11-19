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
	 * Материя отделилась от своей задуманной формы, надо соединить их вновь.
	 *  Быть всем, не состояться ни в чем.
	 */
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(TempestDoubleApplication.class, args);
	}

}
