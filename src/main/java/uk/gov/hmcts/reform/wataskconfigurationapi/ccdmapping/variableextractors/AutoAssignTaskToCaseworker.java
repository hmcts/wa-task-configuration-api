package uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.variableextractors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.ConfigureTaskService;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.Attributes;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.QueryRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.RoleAssignment;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.RoleName;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.RoleType;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.camunda.CamundaValue;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.camunda.TaskResponse;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.IdamSystemTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.roleassignment.RoleAssignmentApi;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Component
public class AutoAssignTaskToCaseworker implements TaskVariableExtractor {

    private final RoleAssignmentApi roleAssignmentApi;
    private final AuthTokenGenerator authTokenGenerator;
    private final IdamSystemTokenGenerator idamSystemTokenGenerator;

    private static final Logger log = LoggerFactory.getLogger(AutoAssignTaskToCaseworker.class);

    public AutoAssignTaskToCaseworker(RoleAssignmentApi roleAssignmentApi,
                                      @Qualifier("ccdServiceAuthTokenGenerator") AuthTokenGenerator authTokenGenerator,
                                      IdamSystemTokenGenerator idamSystemTokenGenerator) {
        this.roleAssignmentApi = roleAssignmentApi;
        this.authTokenGenerator = authTokenGenerator;
        this.idamSystemTokenGenerator = idamSystemTokenGenerator;
    }

    @Override
    public Map<String, Object> getValues(TaskResponse task, Map<String, CamundaValue<Object>> processVariables) {
        String ccdId = (String) processVariables.get(ConfigureTaskService.CCD_ID_PROCESS_VARIABLE_KEY).getValue();

        String s2sToken = authTokenGenerator.generate();
        String userToken = idamSystemTokenGenerator.generate();
        RoleAssignment roleAssignment = roleAssignmentApi.queryRoleAssignments(
            userToken,
            s2sToken,
            buildQueryRequest(ccdId)
        );
        log.info(roleAssignment.toString());
        return Collections.emptyMap();
    }

    private QueryRequest buildQueryRequest(String ccdId) {
        return QueryRequest.builder()
            .roleType(Collections.singletonList(RoleType.CASE.name()))
            .roleName(Collections.singletonList(RoleName.TRIBUNAL_CASEWORKER.getValue()))
            .validAt(LocalDateTime.now())
            .attributes(Collections.singletonMap(Attributes.CASE_ID.getValue(), Collections.singletonList(ccdId)))
            .build();
    }
}
