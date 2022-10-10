package reactdemoapp.backend.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import reactdemoapp.backend.annotations.Subdomain;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;

@AllArgsConstructor
class SubdomainRequestCondition implements RequestCondition<SubdomainRequestCondition> {
    private HashSet<String> subdomains;

    @Override
    public SubdomainRequestCondition combine(SubdomainRequestCondition other) {
        // not relevant
        return null;
    }

    @Override
    public SubdomainRequestCondition getMatchingCondition(HttpServletRequest request) {
        return this.subdomains.stream().anyMatch(subdomain -> request.getServerName().startsWith(subdomain + ".")) ? this : null;
    }

    @Override
    public int compareTo(SubdomainRequestCondition other, HttpServletRequest request) {
        // not relevant
        return 0;
    }
}

class SubdomainRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    @Override
    protected RequestCondition getCustomTypeCondition(Class handlerType) {
        Subdomain subdomain;

        if ((subdomain = AnnotationUtils.findAnnotation(handlerType, Subdomain.class)) != null) {
            return new SubdomainRequestCondition(new HashSet<>(Arrays.asList(subdomain.value())));
        }
        return null;
    }
}

@Component
public class CustomWebMvcRegistrations implements WebMvcRegistrations {
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new SubdomainRequestMappingHandlerMapping();
    }
}
