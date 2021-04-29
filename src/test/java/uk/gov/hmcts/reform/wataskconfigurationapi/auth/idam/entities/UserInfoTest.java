package uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities;

import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

class UserInfoTest {

    @Test
    void isWellImplemented() {
        final Class<?> classUnderTest = UserInfo.class;

        assertPojoMethodsFor(classUnderTest)
            .testing(Method.GETTER)
            .testing(Method.CONSTRUCTOR)
            .testing(Method.EQUALS)
            .testing(Method.HASH_CODE)
            .areWellImplemented();
    }
}
