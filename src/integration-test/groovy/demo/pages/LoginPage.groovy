package demo.pages

import geb.Page
import geb.module.TextInput

class LoginPage extends Page {

    static url = '/login/auth'

    static at = { title.contains('Login') }

    static content = {
        usernameInput { $('input', name: 'username').module(TextInput) }
        passwordInput { $('input', name: 'password') }
        loginButton { $('input', type: 'submit') }
    }

    void login(String username, String password) {
        usernameInput.text = username
        passwordInput << password
        loginButton.click()
    }
}
