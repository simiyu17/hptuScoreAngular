package com.hptu.score.exception.handler;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.exception.CountyAssessmentException;
import com.hptu.score.exception.PillarException;
import com.hptu.score.exception.UserNotAuthenticatedException;
import com.hptu.score.exception.UserNotFoundException;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        return mapExceptionToResponse(exception);
    }

    private Response mapExceptionToResponse(Exception exception) {
        ApiResponseDto responseError = new ApiResponseDto(false, exception.getMessage());
        if (exception instanceof PillarException){
            return Response.status(Response.Status.NOT_FOUND).entity(responseError).build();
        }else if (exception instanceof CountyAssessmentException){
            return Response.status(Response.Status.BAD_REQUEST).entity(responseError).build();
        }else if (exception instanceof UserNotFoundException){
            return Response.status(Response.Status.NOT_FOUND).entity(responseError).build();
        }else if (exception instanceof UserNotAuthenticatedException){
            return Response.status(Response.Status.UNAUTHORIZED).entity(responseError).build();
        }else if (exception instanceof AuthenticationFailedException){
            return Response.status(Response.Status.UNAUTHORIZED).entity(responseError).build();
        }else if (exception instanceof UnauthorizedException){
            return Response.status(Response.Status.UNAUTHORIZED).entity(responseError).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(responseError).build();
    }
}