package org.hse.moodactivities.interceptors;

import io.grpc.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;

public class JWTAuthServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String methodName = serverCall.getMethodDescriptor().getFullMethodName();

        if ("services.AuthService/Login".equals(methodName)) {
            // This is auth, no JWT authorization required here
            return serverCallHandler.startCall(serverCall, metadata);
        }

        String value = metadata.get(JWTUtils.AUTHORIZATION_METADATA_KEY);

        Status status;
        if (value == null) {
            status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
        } else if (!value.startsWith(JWTUtils.BEARER_TYPE)) {
            status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
        } else {
            try {
                String token = value.substring(JWTUtils.BEARER_TYPE.length()).trim();
                Jws<Claims> claims = JWTUtils.getParser().parseSignedClaims(token);
                Context ctx = Context.current().withValue(JWTUtils.CLIENT_ID_CONTEXT_KEY, claims.getPayload().getSubject());
                return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
            } catch (JwtException e) {
                status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
            }
        }

        serverCall.close(status, new Metadata());
        return new ServerCall.Listener<>() {};
    }
}
