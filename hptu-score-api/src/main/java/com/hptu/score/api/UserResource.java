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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response updateUser(@PathParam("userId") Long userId, @Valid UserDto newUser) {
        // TODO complete update
        return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(true, "User Created!!")).build();
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
    public Response changePassword(@Valid UserPassChangeDto userDto) {

        try {
            User u = getCurrentLoggedUser();

            if (Objects.isNull(u)) {
                //return "redirect:/changepassword?error=You must be logged in to change password!";
            }


           /* if (!passwordEncoder.matches(userDto.getPassword(), u.getPassword())) {
                return "redirect:/changepassword?error=Wrong Current Password!";
            }

            if (!userDto.getNewPass().equals(userDto.getPassConfirm())) {
                return "redirect:/changepassword?error=Make sure new password matches its confirmation!";
            }
            if (passwordEncoder.matches(userDto.getNewPass(), u.getPassword())) {
                return "redirect:/changepassword?error=You can not use same password as current!";
            }
            u.setPassword(this.passwordEncoder.encode(userDto.getNewPass()));*/
            //u.setForceChangePass(false);
            //this.userService.createUser(u);
            //return "redirect:/logout";
        } catch (Exception e) {
            //return "redirect:/changepassword?error=An error Occured!!!";
        }

        return Response.ok().build();

    }
}
