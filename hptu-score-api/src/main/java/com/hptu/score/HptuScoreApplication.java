package com.hptu.score;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@QuarkusMain
@ApplicationPath("/hptu-service/api")
public class HptuScoreApplication extends Application {

    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
