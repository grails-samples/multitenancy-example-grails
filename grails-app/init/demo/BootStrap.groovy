package demo

import groovy.transform.CompileStatic

@CompileStatic
class BootStrap {

    UserDataService userDataService

    def init = { servletContext ->
        userDataService.save("sherlock", "elementary", true, false, false, false)

        userDataService.save("watson", "elementary", true, false, false, false)
    }
    def destroy = {
    }
}
