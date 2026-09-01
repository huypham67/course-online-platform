package com.fullstack.online_couse_platform.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private long expirySeconds = 3600;
    private long refreshExpirySeconds = 604800;
}
