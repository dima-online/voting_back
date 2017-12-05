package kz.bsbnb.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Olzhas.Pazyldayev on 30.05.2017.
 */
public final class CustomRequestMatcher implements RequestMatcher {
    private final Log logger = LogFactory.getLog(getClass());

    private final List<RequestMatcher> requestMatchers;

    /**
     * Creates a new instance
     *
     * @param requestMatchers the {@link RequestMatcher} instances to try
     */
    public CustomRequestMatcher(List<RequestMatcher> requestMatchers) {
        Assert.notEmpty(requestMatchers, "requestMatchers must contain a value");
        if (requestMatchers.contains(null)) {
            throw new IllegalArgumentException(
                    "requestMatchers cannot contain null values");
        }
        this.requestMatchers = requestMatchers;
    }

    /**
     * Creates a new instance
     *
     * @param requestMatchers the {@link RequestMatcher} instances to try
     */
    public CustomRequestMatcher(RequestMatcher... requestMatchers) {
        this(Arrays.asList(requestMatchers));
    }

    public CustomRequestMatcher(String... antPatterns) {
        this(RequestMatchers.antMatchers(antPatterns));
    }

    public boolean matches(HttpServletRequest request) {
        return requestMatchers.parallelStream().anyMatch(requestMatcher -> requestMatcher.matches(request));
    }

    @Override
    public String toString() {
        return "AndRequestMatcher [requestMatchers=" + requestMatchers + "]";
    }

    private static final class RequestMatchers {

        /**
         * Create a {@link List} of {@link AntPathRequestMatcher} instances.
         *
         * @param httpMethod  the {@link HttpMethod} to use or {@code null} for any
         *                    {@link HttpMethod}.
         * @param antPatterns the ant patterns to create {@link AntPathRequestMatcher}
         *                    from
         * @return a {@link List} of {@link AntPathRequestMatcher} instances
         */
        public static List<RequestMatcher> antMatchers(HttpMethod httpMethod,
                                                       String... antPatterns) {
            String method = httpMethod == null ? null : httpMethod.toString();
            List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();
            for (String pattern : antPatterns) {
                matchers.add(new AntPathRequestMatcher(pattern, method));
            }
            return matchers;
        }

        /**
         * Create a {@link List} of {@link AntPathRequestMatcher} instances that do not
         * specify an {@link HttpMethod}.
         *
         * @param antPatterns the ant patterns to create {@link AntPathRequestMatcher}
         *                    from
         * @return a {@link List} of {@link AntPathRequestMatcher} instances
         */
        public static List<RequestMatcher> antMatchers(String... antPatterns) {
            return antMatchers(null, antPatterns);
        }

        /**
         * Create a {@link List} of {@link RegexRequestMatcher} instances.
         *
         * @param httpMethod    the {@link HttpMethod} to use or {@code null} for any
         *                      {@link HttpMethod}.
         * @param regexPatterns the regular expressions to create
         *                      {@link RegexRequestMatcher} from
         * @return a {@link List} of {@link RegexRequestMatcher} instances
         */
        public static List<RequestMatcher> regexMatchers(HttpMethod httpMethod,
                                                         String... regexPatterns) {
            String method = httpMethod == null ? null : httpMethod.toString();
            List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();
            for (String pattern : regexPatterns) {
                matchers.add(new RegexRequestMatcher(pattern, method));
            }
            return matchers;
        }

        /**
         * Create a {@link List} of {@link RegexRequestMatcher} instances that do not
         * specify an {@link HttpMethod}.
         *
         * @param regexPatterns the regular expressions to create
         *                      {@link RegexRequestMatcher} from
         * @return a {@link List} of {@link RegexRequestMatcher} instances
         */
        public static List<RequestMatcher> regexMatchers(String... regexPatterns) {
            return regexMatchers(null, regexPatterns);
        }

        private RequestMatchers() {
        }
    }
}