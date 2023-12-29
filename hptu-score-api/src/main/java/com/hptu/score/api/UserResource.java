package com.hptu.score.api;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.dto.UserDto;
import com.hptu.score.dto.UserPassChangeDto;
import com.hptu.score.entity.User;
import com.hptu.score.service.UserService;
import com.hptu.score.util.AuthTokenUtil;
import com.hptu.score.util.CommonUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Path("api/v1/users")
@ApplicationScoped
public class UserResource extends CommonUtil {
    private final UserService userService;
    private final AuthTokenUtil authTokenUtil;

    public UserResource(UserService userService, AuthTokenUtil authTokenUtil) {
        this.userService = userService;
        this.authTokenUtil = authTokenUtil;
    }

    @POST
    @Path("authenticate")
    @PermitAll
    public String logissn() {
        userService.createDefaultUser();
        return "login";
    }



    @POST
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUserForm(UserDto newUser) {
        this.userService.createUser(newUser);
        return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(true, "User Created!!")).build();
    }

    @POST
    @Path("change-password")
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

    @GET
    @Path("token")
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken() {
        return this.authTokenUtil.generateJwt();
    }
}
