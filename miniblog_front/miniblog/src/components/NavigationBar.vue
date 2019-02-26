<template>
    <div id="pads-nav-bar">
        <div class="nav">
            <ul>
                <li class="nav-pad">
                    <router-link to="/">Pads</router-link>
                </li>
                <li>
                    <router-link to="/blog">Blog</router-link>
                </li>
                <li class="login">
                    <a href="javascript:;" class="login_pen"></a>
                    <div class="login_operator">
                        <span @click="logIn">log in</span>
                        <span @click="logOut">log out</span>
                    </div>
                </li>
            </ul>
            <ul>
                <li v-if="isSearchable"><span class="search-icon  fa fa-search"></span>
                    <input id="search" type="text" class="search" v-model="searchKey" @input="searchMe">
                </li>
                <li><a href="https://github.com/Mr-medusa" target="_blank">View On Github</a></li>
            </ul>
        </div>
    </div>
</template>

<script>
    import Vue from "vue"
    import EventHub from "../utils/EventHub"
    import Utils from "../utils/Utils"

    export default {
        name: 'nav-bar',
        props: {
            isSearchable: Boolean,
        },
        data() {
            return {
                searchKey:null,
                lastSearchTime:new Date().getTime(),
                showLogInTip:false,
            }
        },
        methods: {
            searchMe:Utils.debounce(function () {
                EventHub.$emit('navSearchKey', this.searchKey);
            },1000),
            logIn(){
                if(EventHub.user && EventHub.user.username){
                    EventHub.$emit('goTip',["你已经登陆过了!"]);
                }else{
                    EventHub.$emit('logIn');
                }
            },
            logOut(){
                if(!EventHub.user || !EventHub.user.username){
                    EventHub.$emit('goTip',["你还没有登录!"]);
                }else{
                    EventHub.user = null;                               //清空全局的用户信息
                    Vue.http.headers.common['Authorization'] = null;    //重置token
                    sessionStorage.removeItem('miniblogUser');          //移除Session
                    EventHub.refreshPermissionBtn();
                    EventHub.$emit('goTip',["退出成功!"]);
                }
            }
        }
    }
</script>

<style scoped>
    #pads-nav-bar {
        position: fixed;
        height: 60px;
        width: 100%;
        background-color: rgba(0, 0, 0, 0.79);
        z-index: 5555;
        box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);
    }
    #pads-nav-bar .nav {

        display: flex;
    }
    #pads-nav-bar .nav > ul {
        flex: 1;
        display: flex;
        justify-content: center;
    }
    #pads-nav-bar .nav > ul:last-child {
        justify-content: flex-end;
    }
    #pads-nav-bar .nav > ul > li {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-left: 20px;
    }
    #pads-nav-bar .nav > ul:last-child > li:last-child {
        margin-right: 20px;
    }
    #pads-nav-bar .nav > ul > li > a {
        line-height: 60px;
        text-align: center;
    }

    #pads-nav-bar .nav:hover li {
        opacity: 0.4;
    }
    #pads-nav-bar .nav > ul > li:hover {
        cursor: pointer;
        opacity: 1;
    }

    #pads-nav-bar .nav > ul li {
        font-family: "obelixprobrokenregular", Arial, sans-serif;
        font-size: 14px;
    }
    #pads-nav-bar .nav > ul > li a {
        color: rgb(162, 251, 255);
    }
    #pads-nav-bar .nav > ul:nth-child(1) > .nav-pad {
        font-family: "a_callingregular", Arial, sans-serif;
        font-size: 25px;
    }
    #pads-nav-bar .nav > ul:nth-child(1) > .nav-pad a {
        color: #ff8000;
    }

    /*search*/
    #pads-nav-bar .search {
        background-color: rgba(48, 48, 48, 0.75);
        font-family: inconsolatamedium, Arial, sans-serif;
        height: 30px;
        line-height: 30px;
        box-sizing: border-box;
        padding: 0 15px 0 30px;
        color: #a4a4a4;
        outline: none;
        border-radius: 15px;
        margin-right: 10px;
        transition: border-color 0.2s ease;
        background-size: 20px;
        vertical-align: middle !important;
        border-color: rgba(17, 24, 54, 0.69);
    }
    #pads-nav-bar .search-icon {
        transform: translateX(200%);
        color: #636363;
    }

    /*login*/
    #pads-nav-bar .login{
        max-width: 50px;
        margin-left: 50px!important;
        position: relative;
        opacity: 0;
    }
    #pads-nav-bar .login:hover{
        opacity: 1;
    }
    #pads-nav-bar  .login .login_pen{
        display: inline-block;
        width: 30px;
        height: 30px;
        line-height: 60px;
        opacity: 1;
        border-radius: 50%;
        background: url("../assets/img/head.jpg") no-repeat center;
        background-size: 30px 30px;
        transition: all 0.8s ease-in-out;
        cursor: pointer;
    }
    #pads-nav-bar  .login .login_pen:hover{
        transform: rotate(360deg);
    }
    #pads-nav-bar  .login:hover  .login_operator{
        display: flex;
    }
    #pads-nav-bar  .login  .login_operator{
        font-family: sans-serif;
        font-size: 14px;
        display: inline-block;
        position: absolute;
        width: 100px;
        background-color: rgba(45, 45, 45, 0.58);
        top: 50px;
        left: -10px;
        flex-direction: column;
        border-radius: 8px;
        overflow: hidden;
        font-family: 'Comic Sans MS', cursive,'Trebuchet MS', Helvetica, sans-serif;
        display: none;
    }
    #pads-nav-bar  .login  .login_operator span:hover{
        background-color: rgba(204, 106, 67, 0.3);
    }
    #pads-nav-bar  .login  .login_operator span{
        height: 40px;
        line-height: 40px;
        text-align: center;
    }
    #pads-nav-bar  .login  .login_operator span:first-child{
        border-bottom: 1px solid rgba(93, 93, 93, 0.66);
    }
</style>
