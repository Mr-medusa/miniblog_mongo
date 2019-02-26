<template>
    <div id="Frame">
        <!--提示弹出框-->
        <operate-tip v-if="showOperateTip" :result="operateResult" v-model="showOperateTip" :showDuration="showDuration"/>
        <!--登陆弹出框-->
        <transition name="alert-fade">
            <log-in v-if="showLogInTip" v-model="user" v-on:ok="logInOk" v-on:cancel="logInCancel"/>
        </transition>
        <!--导航栏-->
        <NavigationBar :isSearchable="isSearchable"></NavigationBar>
        <!--视图页-->
        <router-view></router-view>
    </div>
</template>

<script>
    import Vue from "vue"
    import NavigationBar from './components/NavigationBar'
    import EventHub from './utils/EventHub'
    import UserService from './server/service/UserService'

    export default {
        name: "Frame",
        components: {
            NavigationBar,
        },
        data() {
            return {
                isSearchable: false,
                /*tip*/
                showOperateTip: false,
                operateResult: {msg: "操作成功!", status: true},
                showDuration: 1500,

                /*login*/
                user:{
                    account:null,
                    password:null,
                },
                showLogInTip:false,
            }
        },
        methods: {
            goTip(params = ["操作成功!",true,1500]) {
                this.operateResult.msg = params[0] || "操作成功!";
                this.operateResult.status = (params[1] === undefined || params[1] === null) ? true : params[1];
                this.showDuration = params[2] || 1500;
                this.showOperateTip = true;
            },
            logIn(){
                this.user = {
                    account:null,
                    password:null,
                    bearer:null,
                };
                this.showLogInTip = true;
            },
            logInOk(){
                var that = this;
                this.showLogInTip = false;
                UserService.userLogin(this.user).then(function (res) {
                    if(res.ok){
                        var token = res.data.token;
                        if(token){
                            Vue.http.headers.common['Authorization'] = `Bearer ${token}`;

                            EventHub.user = {};
                            EventHub.user.username = that.user.account;
                            EventHub.user.bearer = token;
                            EventHub.user.permissions = res.data.permissions;
                            EventHub.refreshPermissionBtn();
                            EventHub.$emit("goTip",['登陆成功!']);

                            sessionStorage.setItem('miniblogUser',JSON.stringify(EventHub.user));
                        }
                    }else{
                        EventHub.$emit("goTip",['登陆失败!',false,3000]);
                    }
                }).catch(function (err) {
                    that.$router.push("/");
                    if(err && err.status === 401){
                        EventHub.$emit("goTip",[err.data.message,false,3000]);
                    }else{
                        EventHub.$emit("goTip",["登陆失败!",false,3000]);
                        console.log(err);
                    }
                });
            },
            logInCancel(){
                this.showLogInTip = false;
            },
        },
        watch: {
            $route: function () {
                if (this.$route.name === "pads")
                    this.isSearchable = false;
                else
                    this.isSearchable = true;
            }
        },
        created: function () {
            EventHub.$on('goTip', this.goTip);
            EventHub.$on('logIn', this.logIn);
        },
        beforeDestroy: function () {
            EventHub.$off('goTip', this.goTip);
            EventHub.$off('logIn', this.logIn);
        },
    }
</script>

<style>
    #Frame {
        height: 100%;
    }
</style>