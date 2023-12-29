package com.hptu.score.exception.handler;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.exception.PillarException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PillarExceptionHandler implements ExceptionMapper<PillarException> {
    @Override
    public Response toResponse(PillarException exception) {
        ApiResponseDto responseError = new ApiResponseDto(false, exception.getMessage());
        if (exception.getHttpStatusCode() == Response.Status.NOT_FOUND.getStatusCode()){
            return Response.status(Response.Status.NOT_FOUND).entity(responseError).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(responseError).build();
    }
}