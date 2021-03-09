package uk.gov.hmcts.reform.wataskconfigurationapi.auth.role;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.IdamTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.Attributes;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.QueryRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.RoleAssignment;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.RoleAssignmentResource;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.RoleName;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.RoleType;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.RoleAssignmentServiceApi;
import uk.gov.hmcts.reform.wataskconfigurationapi.exceptions.ServerErrorException;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class RoleAssignmentService {

    private final AuthTokenGenerator serviceAuthTokenGenerator;

    private final RoleAssignmentServiceApi roleAssignmentServiceApi;

    private final IdamTokenGenerator systemUserIdamToken;

    @Autowired
    public RoleAssignmentService(RoleAssignmentServiceApi roleAssignmentServiceApi,
                                 AuthTokenGenerator serviceAuthTokenGenerator,
                                 IdamTokenGenerator systemUserIdamToken) {
        this.roleAssignmentServiceApi = roleAssignmentServiceApi;
        this.serviceAuthTokenGenerator = serviceAuthTokenGenerator;
        this.systemUserIdamToken = systemUserIdamToken;
    }

    public List<RoleAssignment> searchRolesByCaseId(String caseId) {
        requireNonNull(caseId, "caseId cannot be null");

        RoleAssignmentResource roleAssignmentResponse = performSearch(caseId);

        return roleAssignmentResponse.getRoleAssignmentResponse();
    }


    private RoleAssignmentResource performSearch(String caseId) {
        try {
            final RoleAssignmentResource roleAssignmentResource = roleAssignmentServiceApi.queryRoleAssignments(
                systemUserIdamToken.generate(),
                serviceAuthTokenGenerator.generate(),
                buildQueryRequest(caseId)
            );
            log.info("Roleassignment service details {0}", roleAssignmentResource.toString());
            return roleAssignmentResource;
        } catch (FeignException ex) {
            throw new ServerErrorException(
                "Could not retrieve role assignments when performing the search", ex);
        }
    }

    private QueryRequest buildQueryRequest(String caseId) {
        return QueryRequest.builder()
            .roleType(singletonList(RoleType.CASE))
            .roleName(singletonList(RoleName.TRIBUNAL_CASEWORKER))
            .validAt(LocalDateTime.now())
            .attributes(singletonMap(Attributes.CASE_ID, singletonList(caseId)))
            .build();
    }

}
