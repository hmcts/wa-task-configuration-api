package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

class ClassificationTest {

    @Test
    void isWellImplemented() {
        final Class<?> classUnderTest = Classification.class;

        assertPojoMethodsFor(classUnderTest)
            .testing(Method.GETTER)
            .areWellImplemented();
    }

}
