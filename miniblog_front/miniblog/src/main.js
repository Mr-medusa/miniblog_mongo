import './assets/fonts/a_calling_font_d_by_7ntypes-webfont.woff'
import './assets/fonts/a_calling_font_d_by_7ntypes-webfont.woff2'
import './assets/fonts/inconsolata-webfont.woff'
import './assets/fonts/inconsolata-webfont.woff2'
import './assets/fonts/obelixpro-broken-cyr-webfont.woff'
import './assets/fonts/obelixpro-broken-cyr-webfont.woff2'
import './assets/css/normalize.css'
import './assets/css/util.css'
import './assets/css/hover.css'

import Vue from 'vue'
import VueResource from "vue-resource"
import Frame from './Frame.vue'
import operateTip from './components/modal/OperateTip'
import LogIn from './components/user/LogIn'
import miniSelect from './components/widget/miniSelect'
import EventHub from "./utils/EventHub"
import router from './router'

Vue.config.productionTip = false;

Vue.use(VueResource);
Vue.component('OperateTip', operateTip);
Vue.component('miniSelect', miniSelect);
Vue.component('LogIn', LogIn);

Vue.filter('dateToLocalTime', function (date) {
    if (date instanceof Date)
        return date.toLocaleDateString();
    return date;
});

Vue.filter('timeToSimpleDate', function (date) {
    if (date)
        return date.split(" ")[0];
    return "";
});

/**
 * 初始化用户信息
 */
var user = sessionStorage.getItem('miniblogUser');
if (user) {
    user = JSON.parse(user);
    EventHub.user = user;
    Vue.http.headers.common['Authorization'] = `Bearer ${user.bearer}`;
}

//权限指令
Vue.directive('authorized', {
    bind: function (el) {
        EventHub.allPermissionBtn.set(el,null); //暂时存null
    },
    inserted: function (el) {
        if(EventHub.user && EventHub.user.username && EventHub.user.bearer){
            el.style.display = '';
        } else {
            el.style.display = 'none';
        }
    },
    unbind: function (el) {
        EventHub.allPermissionBtn.delete(el);
    }
});

new Vue({
    router,
    render: h => h(Frame),
}).$mount('#app');
