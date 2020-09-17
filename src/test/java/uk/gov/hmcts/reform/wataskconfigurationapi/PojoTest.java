package uk.gov.hmcts.reform.wataskconfigurationapi;

import com.google.common.testing.EqualsTester;
import com.jparams.verifier.tostring.ToStringVerifier;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.ClassAndFieldPredicatePair;
import pl.pojo.tester.api.assertion.Method;
import pl.pojo.tester.internal.field.DefaultFieldValueChanger;
import pl.pojo.tester.internal.instantiator.ObjectGenerator;
import pl.pojo.tester.internal.utils.ThoroughFieldPermutator;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.AddLocalVariableRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.CamundaValue;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.ConfigureTaskRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.DmnRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.MapCaseDataDmnRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.MapCaseDataDmnResult;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.TaskResponse;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsForAll;

@SuppressWarnings({
    "unchecked",
    "PMD.JUnitTestsShouldIncludeAssert",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.EqualsNull",
    "PMD.AvoidInstantiatingObjectsInLoops"
})
class PojoTest {

    private final Class[] classesToTest = {
        ConfigureTaskRequest.class,
        MapCaseDataDmnRequest.class,
        DmnRequest.class,
        MapCaseDataDmnResult.class,
        CamundaValue.class,
        AddLocalVariableRequest.class,
        TaskResponse.class
    };
    // Cannot test equals for generic classes
    private final Class[] ignoreEquals = {
        DmnRequest.class,
        MapCaseDataDmnRequest.class,
        CamundaValue.class
    };
    private final ObjectGenerator objectGenerator = new ObjectGenerator(
        DefaultFieldValueChanger.INSTANCE,
        new ArrayListValuedHashMap<>(),
        new ThoroughFieldPermutator()
    );
    private final EqualsTester equalsTester = new EqualsTester();

    @Test
    void allPojosAreWellImplemented() {
        assertPojoMethodsForAll(
            classesToTest
        )
            .testing(Method.GETTER)
            .testing(Method.CONSTRUCTOR)
            .areWellImplemented();
    }

    @Test
    void equalsTest() {
        for (Class classUnderTest : classesToTest) {
            Object newInstance = objectGenerator.createNewInstance(classUnderTest);
            equalsTester.addEqualityGroup(newInstance).testEquals();
            if (!asList(ignoreEquals).contains(classUnderTest)) {
                Object anotherInstance = objectGenerator.createNewInstance(classUnderTest);
                assertThat(
                    "Check instance: " + newInstance + "\nequals another instance: " + anotherInstance,
                    newInstance.equals(anotherInstance),
                    is(true)
                );
                List<Object> differentObjects = objectGenerator.generateDifferentObjects(new ClassAndFieldPredicatePair(
                    classUnderTest));
                //Yeah I know!
                List<Object> reallyDifferentObjects = differentObjects.stream()
                    .filter(differentObject -> !differentObject.equals(newInstance))
                    .collect(toList());
                for (Object reallyDifferentObject : reallyDifferentObjects) {
                    assertThat(
                        "Check instance does not equal another instance that is different \n"
                        + newInstance + "\n"
                        + reallyDifferentObject,
                        newInstance.equals(reallyDifferentObject),
                        is(false)
                    );
                }
            }
        }
    }

    @Test
    void checkHashCodeDoesNotChange() {
        for (Class classUnderTest : classesToTest) {
            Object newInstance = objectGenerator.createNewInstance(classUnderTest);
            assertThat("Hashcode does not change", newInstance.hashCode(), is(newInstance.hashCode()));
        }
    }

    @Test
    void verifyToStringTest() {
        ToStringVerifier.forClasses(classesToTest).verify();
    }
}
