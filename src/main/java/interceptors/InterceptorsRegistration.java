package interceptors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Tountoun Abel AYANOU
 * @version 1.0
 * @since 1/08/2022
 */
@Component
@RequiredArgsConstructor
public class InterceptorsRegistration implements WebMvcConfigurer {
    private Interceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
