package comandareal.app.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void init() {
        // Carrega variáveis de ambiente do arquivo .env se existir
        try {
            Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
            
            // Define system properties para que o Spring Boot possa usar
            dotenv.entries().forEach(entry -> {
                if (System.getProperty(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });
            
        } catch (Exception e) {
            // Ignora erros, o projeto continuará funcionando com variáveis de ambiente do sistema
            System.out.println("⚠️  Arquivo .env não encontrado ou erro ao carregar. Usando variáveis de ambiente do sistema.");
        }
    }
}