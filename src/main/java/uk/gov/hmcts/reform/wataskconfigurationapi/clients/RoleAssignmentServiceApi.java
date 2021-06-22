package uk.gov.hmcts.reform.wataskconfigurationapi.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.request.MultipleQueryRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities.response.RoleAssignmentResource;
import uk.gov.hmcts.reform.wataskconfigurationapi.config.FeignConfiguration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.wataskconfigurationapi.config.ServiceTokenGeneratorConfiguration.SERVICE_AUTHORIZATION;

@FeignClient(
    name = "role-assignment-service-api",
    url = "${role-assignment-service.url}",
    configuration = FeignConfiguration.class
)
public interface RoleAssignmentServiceApi {
    String V2_MEDIA_TYPE_POST_ASSIGNMENTS =
        "application/vnd.uk.gov.hmcts.role-assignment-service"
        + ".post-assignment-query-request+json;charset=UTF-8;version=2.0";

    @PostMapping(
        value = "/am/role-assignments/query",
        consumes = V2_MEDIA_TYPE_POST_ASSIGNMENTS,
        produces = V2_MEDIA_TYPE_POST_ASSIGNMENTS)
    RoleAssignmentResource queryRoleAssignments(
        @RequestHeader(AUTHORIZATION) String userToken,
        @RequestHeader(SERVICE_AUTHORIZATION) String s2sToken,
        @RequestBody MultipleQueryRequest queryRequest
    );

}
