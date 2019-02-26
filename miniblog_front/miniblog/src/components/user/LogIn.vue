<template>
    <div class="operator-modal modal-mask">
        <div class="modal-container">
            <div class="modal">
                <h3 class="title">log in</h3>
                <div class="halving-line"></div>
                <div class="content">
                    <input class="form-control" type="text" autocomplete="off" placeholder="account..." maxlength="10" v-model="user.account" @input="$emit('input',user)">
                    <input class="form-control" type="password" autocomplete="off"  placeholder="password..." maxlength="10" v-model="user.password" @input="$emit('input',user)">
                </div>
                <div class="friendly-reminder">此系统仅供个人使用,想用的朋友可以到GitHub上下载!</div>
                <div class="modal-actions">
                    <button class="modal-cancel" @click="$emit('cancel')">Cancel</button>
                    <button class="modal-ok" @click="$emit('ok')">Ok!</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "LogIn",
        props: {'width': Number, "alertTitle": String},
        data(){
            return {
                user:{
                    account:null,
                    password:null
                }
            }
        }
    }
</script>

<style scoped>
    .operator-modal {
        position: fixed;
        top: 0;
        right: 0;
        left: 0;
        bottom: 0;
        z-index: 9999;
    }

    .modal-mask {
        background: radial-gradient(circle at center, rgba(56, 56, 56, 0.4) 0, rgba(0, 0, 0, 0.8) 100%);
        will-change: opacity;
    }

    .modal-container {
        color: white;
        font-size: 16px;
        height: 100%;
        width: 100%;
    }

    button {
        cursor: pointer;
        color: white;
        font-size: 20px;
        border: none;
        outline: none;
        background-color: transparent;
    }

    h3 {
        font-size: 1em;
        line-height: 1em;
    }

    .title {
        color: #4ec9b4;
    }

    .content {
        margin: 30px 0;
        align-self: flex-end;
        width: 100%;
    }

    .modal-cancel {
        color: rgb(224, 115, 35);
    }

    .modal-ok {
        color: #4fff22;
    }

    .modal {
        opacity: 1;
        box-shadow: 0 10px 50px rgba(0, 0, 0, 0.3);
        background: rgba(7, 7, 7, 0.85);
        border-radius: 10px;
        padding: 0 30px 20px 35px;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        color: #919191;
        width: 300px;
        margin: 200px auto;
    }

    .modal-actions {
        align-self: flex-end;
    }

    .halving-line {
        width: 100%;
        height: 1px;
        background-color: rgba(92, 92, 92, 0.36);
    }

    input {
        width: 100%;
        color: #ececec;
        background-color: rgba(21, 21, 21, 0.55);
        font-size: 14px;
        border: 1px solid #2b2b2b;

    }
    input:first-child {
        margin-bottom: 10px;
    }
    .friendly-reminder{
        font-size: 12px;
        font-family: STKaiti,LiSu,STXinwei;
        color: #89cc71;
        margin: 0 20px;
    }
</style>