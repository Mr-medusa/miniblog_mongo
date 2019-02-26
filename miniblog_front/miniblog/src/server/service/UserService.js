import Vue from "vue"

const USER_LOGIN = "/miniblog/login";

export default {
    userLogin: function (user) {
        return Vue.http.post(USER_LOGIN, {
            username:user.account,
            password:user.password
        });
    }
}

