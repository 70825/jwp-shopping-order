package cart;

import cart.dao.MemberDao;
import cart.ui.MemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberDao memberDao;

    public WebMvcConfig(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberDao));
    }
}
