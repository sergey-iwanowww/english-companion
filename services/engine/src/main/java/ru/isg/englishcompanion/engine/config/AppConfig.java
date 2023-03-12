package ru.isg.englishcompanion.engine.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"ru.isg.englishcompanion.engine", "ru.isg.englishcompanion.common"})
@EnableScheduling
public class AppConfig {

}
