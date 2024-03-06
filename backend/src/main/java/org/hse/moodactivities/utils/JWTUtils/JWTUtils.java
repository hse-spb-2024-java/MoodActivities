package org.hse.moodactivities.utils.JWTUtils;

import io.github.cdimascio.dotenv.Dotenv;
import io.grpc.Context;
import io.grpc.Metadata;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JWTUtils {
    public static final SecretKey JWT_SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Dotenv.load().get("JWT_SIGNING_KEY")));
    public static final String BEARER_TYPE = "Bearer";
    public static final int TOKEN_TIME_TO_LIVE_MINUTES = 15;

    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

    private static final JwtParserBuilder parserBuilder;

    private static final JwtBuilder jwtBuilder;


    static {
        parserBuilder = Jwts.parser();
        parserBuilder.verifyWith(JWT_SIGNING_KEY);

        jwtBuilder = Jwts.builder();
        jwtBuilder.signWith(JWT_SIGNING_KEY);
    }

    public static JwtParser getParser() {
        return parserBuilder.build();
    }

    public static JwtBuilder getBuilder() {
        var local_now = LocalDateTime.now();
        var local_expiration = local_now.plus(Duration.of(TOKEN_TIME_TO_LIVE_MINUTES, ChronoUnit.MINUTES));
        return jwtBuilder
                .issuedAt(Date.from(local_now.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(local_expiration.atZone(ZoneId.systemDefault()).toInstant()));
    }
}
