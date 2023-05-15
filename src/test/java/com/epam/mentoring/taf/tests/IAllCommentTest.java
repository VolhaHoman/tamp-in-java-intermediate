package com.epam.mentoring.taf.tests;

import org.testng.annotations.BeforeClass;

public interface IAllCommentTest {

    @BeforeClass
    default void comments() {
        AuthorizationUserBase.authorization();
        AllCommentsBase.getSlug();
    }
}
