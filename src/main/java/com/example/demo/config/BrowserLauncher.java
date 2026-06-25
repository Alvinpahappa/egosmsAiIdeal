package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher implements ApplicationRunner {

    @Value("${server.port:8080}")
    private int port;

    @Override
    public void run(ApplicationArguments args) {
        String url = "http://localhost:" + port + "/";
        System.out.println();
        System.out.println("==================================================");
        System.out.println("  EgoSMS Message Coach UI is ready");
        System.out.println("  Open in browser: " + url);
        System.out.println("==================================================");
        System.out.println();

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
            }
        } catch (Exception ignored) {
            // Headless or restricted environment — URL is printed above
        }
    }
}
