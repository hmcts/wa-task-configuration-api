package uk.gov.hmcts.reform.wataskconfigurationapi.entities;

import org.junit.Test;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.TestType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTypeTest {

    @Test
    public void has_correct_asylum_appeal_types() {
        assertEquals(TestType.from("yes").get(), TestType.YES);
        assertEquals(TestType.from("no").get(), TestType.NO);
    }

    @Test
    public void returns_optional_for_unknown_appeal_type() {
        assertEquals(TestType.from("some_unknown_type"), Optional.empty());
    }

    @Test
    public void if_this_test_fails_it_is_because_it_needs_updating_with_your_changes() {
        assertEquals(2, TestType.values().length);
    }
}
