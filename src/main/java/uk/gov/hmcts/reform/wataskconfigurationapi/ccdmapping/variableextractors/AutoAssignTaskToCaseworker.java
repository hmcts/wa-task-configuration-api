package uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.variableextractors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
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
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.roleassignment.RoleAssignmentClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order
public class AutoAssignTaskToCaseworker implements TaskVariableExtractor {

    private final RoleAssignmentClient roleAssignmentClient;
    private final AuthTokenGenerator authTokenGenerator;
    private final IdamSystemTokenGenerator idamSystemTokenGenerator;

    private static final Logger LOG = LoggerFactory.getLogger(AutoAssignTaskToCaseworker.class);

    public AutoAssignTaskToCaseworker(RoleAssignmentClient roleAssignmentClient,
                                      @Qualifier("ccdServiceAuthTokenGenerator") AuthTokenGenerator authTokenGenerator,
                                      IdamSystemTokenGenerator idamSystemTokenGenerator) {
        this.roleAssignmentClient = roleAssignmentClient;
        this.authTokenGenerator = authTokenGenerator;
        this.idamSystemTokenGenerator = idamSystemTokenGenerator;
    }

    @Override
    public Map<String, Object> getValues(TaskResponse task, Map<String, CamundaValue<Object>> processVariables) {
        String ccdId = (String) processVariables.get(ConfigureTaskService.CCD_ID_PROCESS_VARIABLE_KEY).getValue();

        List<RoleAssignment> roleAssignmentList = roleAssignmentClient.queryRoleAssignments(
            idamSystemTokenGenerator.generate(),
            authTokenGenerator.generate(),
            buildQueryRequest(ccdId)
        );

        Map<String, Object> mappedDetails = new ConcurrentHashMap<>();

        if (roleAssignmentList.isEmpty()) {
            LOG.debug("Role assignment not found: {}", roleAssignmentList.toString());
            mappedDetails.put("taskState", "Unassigned");
        } else {
            LOG.debug("Role assignment found: {}", roleAssignmentList.toString());
            // String actorId = roleAssignmentList.get(0).getActorId();
            //todo: call the /task/id/assignee camunda api to set assignee
            mappedDetails.put("taskState", "Assigned");
        }

        return mappedDetails;
    }

    private QueryRequest buildQueryRequest(String ccdId) {
        return QueryRequest.builder()
            .roleType(Collections.singletonList(RoleType.CASE))
            .roleName(Collections.singletonList(RoleName.TRIBUNAL_CASEWORKER))
            .validAt(LocalDateTime.now())
            .attributes(Collections.singletonMap(Attributes.CASE_ID, Collections.singletonList(ccdId)))
            .build();
    }
}
