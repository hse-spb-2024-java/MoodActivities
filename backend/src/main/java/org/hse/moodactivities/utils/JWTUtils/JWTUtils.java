package org.hse.moodactivities.utils.JWTUtils;

import io.github.cdimascio.dotenv.Dotenv;
import io.grpc.Context;
import io.grpc.Metadata;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JWTUtils {
    public static final SecretKey JWT_SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Dotenv.load().get("JWT_SIGNING_KEY")));
    public static final String BEARER_TYPE = "Bearer";

    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

    private static final JwtParserBuilder parserBuilder;

    static {
        parserBuilder = Jwts.parser();
        parserBuilder.verifyWith(JWT_SIGNING_KEY);
    }

    public static JwtParser getParser() {
        return parserBuilder.build();
    }
}
