package com.hptu.score.api;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.dto.AuthRequestDto;
import com.hptu.score.dto.UserDto;
import com.hptu.score.dto.UserPassChangeDto;
import com.hptu.score.entity.User;
import com.hptu.score.service.UserService;
import com.hptu.score.util.CommonUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.Objects;

@Path("/v1/users")
@ApplicationScoped
public class UserResource extends CommonUtil {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @RolesAllowed("Admin")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @POST
    @Path("/authenticate")
    @PermitAll
    public Response authenticate(@Valid AuthRequestDto authRequestDto) {
        userService.createDefaultUser();
        return Response.ok(userService.authenticateUser(authRequestDto)).build();
    }

    @POST
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(@Valid UserDto newUser) {
        this.userService.createUser(newUser);
        return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(true, "User Created!!")).build();
    }


    @PUT
    @Path("{userId}")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") Long userId, UserDto newUser) {
        this.userService.updateUser(userId, newUser);
        return Response.ok(new ApiResponseDto(true, "User Updated !!")).build();
    }

    @GET
    @Path("{userId}")
    @RolesAllowed("Admin")
    public Response getUserById(@PathParam("userId") Long userId) {
        return Response.ok(userService.findUserById(userId)).build();
    }



    @POST
    @Path("change-password")
    @RolesAllowed("Admin")
    public Response changePassword(@Valid UserPassChangeDto userDto, @Context SecurityContext ctx) {
            User u = getCurrentLoggedUser(ctx);
            this.userService.updateUserPassword(u, userDto);
            return Response.ok(new ApiResponseDto(true, "User Password Updated !!")).build();

    }
}
