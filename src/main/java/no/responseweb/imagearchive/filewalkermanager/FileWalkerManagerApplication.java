package no.responseweb.imagearchive.filewalkermanager;

import lombok.extern.slf4j.Slf4j;
import no.responseweb.imagearchive.filestoredbservice.config.DBModuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import(DBModuleConfig.class)
public class FileWalkerManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileWalkerManagerApplication.class, args);
	}

}


