package sn.sonatel.dsi.ins.ftsirc.security.oauth2;

import java.util.Collection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.ins.ftsirc.security.SecurityUtils;

@Component
public class JwtGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public JwtGrantedAuthorityConverter() {
        // Bean extracting authority.
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return SecurityUtils.extractAuthorityFromClaims(jwt.getClaims());
    }
}
