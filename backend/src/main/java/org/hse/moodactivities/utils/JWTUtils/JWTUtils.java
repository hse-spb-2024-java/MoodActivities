package org.hse.moodactivities.utils.JWTUtils;

import io.github.cdimascio.dotenv.Dotenv;
import io.grpc.Context;
import io.grpc.Metadata;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

public class JWTUtils {
    public static final SignatureAlgorithm alg = Jwts.SIG.RS512;
    public static final String BEARER_TYPE = "Bearer";
    public static final int TOKEN_TIME_TO_LIVE_MINUTES = 15;
    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

    private static final JwtParserBuilder parserBuilder;

    private static final JwtBuilder jwtBuilder;
    private static final PrivateKey JWT_PRIVATE_KEY;

    public static final PublicKey JWT_PUBLIC_KEY;


    static {
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(Dotenv.load().get("JWT_PRIVATE_KEY")));
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(Dotenv.load().get("JWT_PUBLIC_KEY")));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Use "RSA", "DSA", or "EC" depending on your key type
            JWT_PRIVATE_KEY = keyFactory.generatePrivate(privateKeySpec);
            JWT_PUBLIC_KEY = keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Realistically impossible situation
            throw new RuntimeException(e);
        }

        parserBuilder = Jwts.parser();
        parserBuilder.verifyWith(JWT_PUBLIC_KEY);

        jwtBuilder = Jwts.builder();
        jwtBuilder.signWith(JWT_PRIVATE_KEY);
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
