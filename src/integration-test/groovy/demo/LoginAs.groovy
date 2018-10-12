package demo

import groovy.transform.CompileStatic
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder

@CompileStatic
trait LoginAs {

    abstract UserDataService getUserDataService()

    void loginAs(String username, String authority = null) {
        User user = userDataService.findByUsername(username)
        if ( user ) {
            List<GrantedAuthority> authorityList = authority ? AuthorityUtils.createAuthorityList(authority) : []
            SecurityContextHolder.context.authentication = new UsernamePasswordAuthenticationToken(user.username,
                    user.password,
                    authorityList)
        }
    }
}