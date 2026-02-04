package dev.madela.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "dev.madela") // <-- Указывает пакет, где искать @Service, @Repository и т.д.
public class ServiceConfig {
    // Пока что пустая конфигурация, вся работа делается через @ComponentScan
}