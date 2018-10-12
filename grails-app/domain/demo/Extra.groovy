package demo

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@GrailsCompileStatic
class Extra implements MultiTenant<Extra> {
    String name

    static constraints = {
        name nullable: false, blank: false
    }
}
