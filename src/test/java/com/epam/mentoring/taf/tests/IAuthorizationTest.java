package com.epam.mentoring.taf.tests;

import org.testng.annotations.BeforeClass;

public interface IAuthorizationTest {

    @BeforeClass
    default void auth() {
        AuthorizationUserBase.authorization();
    }
}
