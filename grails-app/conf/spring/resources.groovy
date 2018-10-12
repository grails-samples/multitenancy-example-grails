import demo.CustomTenantResolver
import demo.UserPasswordEncoderListener
// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    customtenantresolver(CustomTenantResolver)
}
