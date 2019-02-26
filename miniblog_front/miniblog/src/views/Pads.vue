<template>
        <div id="pads" class="pads">
            <!--文件夹弹出框-->
            <transition name="alert-fade">
                <operator-modal v-if="folderAlert"
                                v-on:createFolderAlertOK="createFolderAlertOK"
                                v-on:createFolderAlertCancel="createFolderAlertCancel"
                                v-on:deleteFolderAlertOK="deleteFolderAlertOK"
                                v-on:deleteFolderAlertCancel="deleteFolderAlertCancel"
                                v-on:editFolderAlertOK="editFolderAlertOK"
                                v-on:editFolderAlertCancel="editFolderAlertCancel"
                                v-on:addPadAlertOK="addPadAlertOK"
                                v-on:addPadAlertCancel="addPadAlertCancel"
                                :width="alertWidth"
                                :alertTitle="alertTitle"
                                :alertCallBackName="alertCallBackName"
                >
                    <template slot="content">
                        <component v-bind:is="alertComponent"
                                   v-model="newFolderOrPad"
                                   :defaultOpt="defaultOpt"
                                   :alertMsg="alertMsg"
                                   :newFileName="newFileName"
                                   :classify="classify"
                                   :orders="orders"
                        >
                        </component>
                    </template>
                </operator-modal>
            </transition>
            <!--侧边栏-->
            <div class="pads-sidebar">
                <div class="pads-operator">
                    <div class="folder-operator-btn-group">
                        <div class="icon-group">
                            <span class="fa folder-operator hvr-wobble-top" :class="{'fa-expand':!collapseAll,'fa-compress ':collapseAll}" title="switch to" @click="collapseAll = !collapseAll"></span>
                        </div>
                        <div class="icon-group">
                            <span v-authorized class="fa fa-trash-o folder-operator hvr-wobble-top" title="delete folder" @click="delFolder"></span>
                            <span v-authorized class="fa fa-pencil-square-o folder-operator hvr-wobble-top" title="edit folder" @click="updateFolder"></span>
                            <span v-authorized class="fa fa-plus-square-o folder-operator hvr-wobble-top" title="new folder" @click="newFolder"></span>
                        </div>
                    </div>
                </div>
                <!--侧边栏-->
                <div id="scroll-box">
                    <div class="protector">
                        <div v-for="folder in folders">
                            <folder-tree :folder="folder"
                                         :colors="colors"
                                         :selectedState="selectedState"
                                         :parent="null"
                                         :collapseAll="collapseAll"></folder-tree>
                        </div>
                    </div>
                </div>
            </div>
            <!--内容区-->
            <div class="pads-content">
                <pad-operator-button  v-on:addPad="addPad" :folders="folders"></pad-operator-button>
                <div class="pad-grid-pen">
                    <pad-card></pad-card>
                </div>
            </div>
         </div>
</template>

<script>
    import '../assets/css/pads.css'
    import '../assets/css/cus-codemirror.css'

    import EventHub from "../utils/EventHub"
    import Utils from "../utils/Utils"
    import FrontConfig from "../config/FrontConfig"

    import folderTree from '../components/folder/FolderTree'
    import UpdatePadModal from '../components/modal/UpdatePadModal.vue'
    import deleteFolderOrPad from '../components/modal/DeleteFolderOrPad'
    import operatorModal from '../components/modal/OperatorModal.vue'
    import padCard from '../components/pads/PadCard'
    import padOperatorButton from '../components/pads/PadOperatorButton'

    import PadsService from "../server/service/PadsService";

    export default {
        name: 'Pads',
        components: {
            folderTree,
            operatorModal,
            UpdatePadModal,
            deleteFolderOrPad,
            padCard,
            padOperatorButton
        },
        mounted() {
            Utils.makeScrollBar("#scroll-box", {scrollInertia: 300, deltaFactor: 100, axis: "y"});
            //使用该函数初始化数据
            this.initIndexDBCompleted();
        },
        data() {
            return {
                /*侧边栏是否折叠所有目录*/
                collapseAll: FrontConfig.sideBarCollapse,

                /*侧边栏*/
                folderAlert: false,
                newFileName: "",
                newFolderOrPad: {
                    name:'',
                    type:'',
                    order:'',
                },
                defaultOpt:{
                    isFolder:false,
                    isDefault:false,
                },
                alertTitle: null,
                alertWidth: 300,
                alertMsg: null,
                alertComponent: null,
                alertCallBackName: [],
                selectedState: {
                    styler: {},
                    folder: {}
                },
                openState:{},
                showSomeFolder:{},
                folders: [],
                colors: EventHub.colors,

                newPadName: "",

                /*type*/
                classify:EventHub.classify,
                orders:[],
             }
        },
        methods: {
            /*目录 CRUD*/
            newFolder() {
                this.alertTitle = "create folder";
                this.refreshNewFolderOrPadParams(true);
                this.alertComponent = 'UpdatePadModal';
                this.alertWidth = 300;
                this.alertCallBackName = ['createFolderAlertOK', 'createFolderAlertCancel'];
                this.newFileName = null;
                this.folderAlert = true;

                this.orders = [];

                var parent = this.selectedState.folder.id != null ? this.selectedState.folder : {children: this.folders};
                EventHub.getTypes(this.selectedState.folder);
                EventHub.getOrders(this.selectedState.folder,this.orders,parent);
            },
            createFolderAlertOK() {
                if (!this.newFolderOrPad.name || !this.newFolderOrPad.name.trim())
                    this.newFolderOrPad.name = "UntitledFolder";

                var newFolder = this.newFolderOrPadObj("Folder");

                var parent = this.selectedState.folder.name ? this.selectedState.folder:{ children:this.folders};
                parent.isStretch = true;

                 var order = this.newFolderOrPad.order;

                newFolder.parentId = parent.id;
                newFolder.name = this.newFolderOrPad.name;
                newFolder.type =  this.newFolderOrPad.type.name;
                newFolder.isDefault = this.newFolderOrPad.isDefault;
                newFolder.beCreate = true;

                if(order.id){
                    EventHub.insertPad(newFolder,parent,order.id);      //插入目录或Pad到指定的位置
                }else{
                    newFolder.order = parent.children.length;
                    parent.children.push(newFolder);
                }

                this.folderAlert = false;

                var needMoreOption = null;

                /*** Default Folder Start ***/
                if(this.newFolderOrPad.isDefault){
                    var defaultFolder = EventHub.defaultFolder;
                    defaultFolder.isDefault = false;

                    EventHub.defaultFolderId = newFolder.id;
                    EventHub.defaultFolder = newFolder;

                    needMoreOption = ()=> PadsService.put(defaultFolder);
                }
                /*** Default Folder End ***/

                /*** Persistence Data ***/
                if(!order.id){
                    this.putPad(newFolder,{isTip:true,successful:"新建文件夹成功!",unsuccessful:"新建文件夹失败!"},needMoreOption);
                }else{

                    this.putPadToSepciPlace({
                        pad:newFolder,
                        orderId:order.id
                    },{isTip:true,successful:"新建文件夹成功!",unsuccessful:"新建文件夹失败!"},needMoreOption);
                }
                /*** Persistence Data ***/
            },
            createFolderAlertCancel() {
                this.folderAlert = false;
            },

            delFolder() {
                this.alertTitle = "delete folder";
                this.alertComponent = 'deleteFolderOrPad';
                this.alertWidth = 175;
                if (this.selectedState.folder.name && this.selectedState.folder.children.length == 0){
                    this.alertMsg = "确定删除!";
                    this.alertCallBackName = ['deleteFolderAlertOK', 'deleteFolderAlertCancel']
                    this.folderAlert = true;
                }
                else{
                    EventHub.$emit('goTip',["请选择一个空文件夹",false,3000]);
                }
            },
            deleteFolderAlertOK() {
                if (this.selectedState.folder.name && this.selectedState.folder.children.length != 0 || this.selectedState.folder.id == EventHub.defaultFolderId){
                    this.folderAlert = false;
                    EventHub.$emit("goTip",["默认文件夹不能删除!",false]);
                    return;
                }

                var id = this.selectedState.folder.id;
                var children = this.folders;
                this.deletedFolder(children,id);

                /*** Persistence Data ***/
                PadsService.delete(id).then(function () {
                    EventHub.$emit("goTip",["删除成功!"])
                }).catch(function () {
                    EventHub.$emit("goTip",["删除失败!",false])
                });
                /*** Persistence Data ***/

                this.selectedState.folder = {};
                this.folderAlert = false;
            },
            deleteFolderAlertCancel() {
                this.folderAlert = false;
            },

            updateFolder() {
                this.alertTitle = "edit folder";
                this.alertWidth = 300;
                this.alertCallBackName = ['editFolderAlertOK', 'editFolderAlertCancel'];
                this.refreshNewFolderOrPadParams(true,this.selectedState.folder.isDefault);
                this.folderAlert = true;

                if (!this.selectedState.folder.name){
                    this.alertComponent = 'deleteFolderOrPad';
                    this.alertMsg = "你还没有选择文件夹!";
                }else{
                    this.alertComponent = 'UpdatePadModal';
                    this.newFileName = this.selectedState.folder.name;
                    this.orders = [];

                    var parent = EventHub.getPadParent(this.selectedState.folder);
                    EventHub.getOrders(this.selectedState.folder,this.orders,parent);
                    EventHub.getTypes(this.selectedState.folder);
                }
            },
            editFolderAlertOK() {
                var persisPads = [];
                if (this.selectedState.folder.name){
                    this.selectedState.folder.name = this.newFolderOrPad.name;
                    this.selectedState.folder.type = this.newFolderOrPad.type?this.newFolderOrPad.type.name:null;

                    if(this.selectedState.folder.id != EventHub.defaultFolderId && this.newFolderOrPad.isDefault){

                        var defaultFolder = EventHub.defaultFolder;
                        defaultFolder.isDefault = false;

                        this.selectedState.folder.isDefault = true;
                        EventHub.defaultFolderId = this.selectedState.folder.id;
                        EventHub.defaultFolder = this.selectedState.folder;

                        persisPads.push(defaultFolder);
                    }

                    //交换位置
                    if(this.newFolderOrPad.order && this.newFolderOrPad.order.id != this.selectedState.folder.id){
                        persisPads.push(...EventHub.interchange(this.selectedState.folder,this.newFolderOrPad.order.id));
                    }else{
                        persisPads.push(this.selectedState.folder);
                    }

                    /*** Persistence Data ***/
                    PadsService.updatePad(persisPads).then(function () {
                        EventHub.$emit('goTip',["修改文件夹成功!"]);
                    }).catch(function () {
                        EventHub.$emit('goTip',["修改文件夹失败!",false,3500]);
                    });
                    /*** Persistence Data ***/
                }

                this.folderAlert = false;
            },
            editFolderAlertCancel() {
                this.folderAlert = false;
            },

            /*pad CRUD*/
            addPad() {
                this.alertTitle = "create pad";
                this.refreshNewFolderOrPadParams(false);
                this.alertCallBackName = ['addPadAlertOK', 'addPadAlertCancel'];
                this.folderAlert = true;
                this.alertWidth = 300;
                this.alertComponent = 'UpdatePadModal';
                this.newFileName = null;
                this.orders = [];
                if(this.selectedState.folder.name){
                    EventHub.getOrders(this.selectedState.folder,this.orders,this.selectedState.folder);
                }else{
                    EventHub.getOrders(this.selectedState.folder,this.orders,EventHub.defaultFolder);
                }
                var typeObj = this.selectedState.folder.name ? this.selectedState.folder : EventHub.defaultFolder;
                EventHub.getTypes(typeObj);
            },
            addPadAlertOK() {
                var parent = this.selectedState.folder.id?this.selectedState.folder:EventHub.defaultFolder;
                parent.isStretch = true;

                var newPad = this.newFolderOrPadObj();
                newPad.name = this.newFolderOrPad.name ||  "untitled_pad";
                newPad.type = this.newFolderOrPad.type.name;
                newPad.parentId = parent.id;
                newPad.editable = true;

                var order = this.newFolderOrPad.order;

                EventHub.insertPad(newPad,parent,order?order.id:null);

                PadsService.putCard(newPad);

                if(order.id){
                    this.putPadToSepciPlace({
                        pad:newPad,
                        orderId:order.id
                    },{isTip:true,successful:"新建Pad成功!",unsuccessful:"新建Pad失败!"},function () {
                        EventHub.$emit('addPadCard', newPad);
                    });
                }else{
                    newPad.order = parent.children.length;
                    this.putPad(newPad,{isTip:true,successful:"新建pad成功!",unsuccessful:"新建pad失败!"},function () {
                        EventHub.$emit('addPadCard', newPad);
                    });
                }

                this.folderAlert = false;
            },
            addPadAlertCancel() {
                this.folderAlert = false;
            },

            /*Helper*/
            newFolderOrPadObj(type) {
                return {
                    parentId: null,
                    id: Utils.UUID2(16,16),
                    name: type === "Folder" ? this.newFileName : this.newPadName,
                    isShow: true,
                    isStretch: true,
                    isChecked: false,
                    children: type === "Folder" ? [] : null,
                    createTime:Utils.simpleDateFormat(new Date()),
                    updateTime:Utils.simpleDateFormat(new Date())
                };
            },
            deletedFolder(folders, delId) {
                for (var i = 0; i < folders.length; i++) {
                    if (delId == folders[i].id) {
                        folders.splice(i, 1);
                        return true;
                    } else {
                        if (folders[i].children != null && folders[i].children.length > 0) {
                            this.deletedFolder(folders[i].children, delId);
                        }
                    }
                }
            },
            refreshNewFolderOrPadParams(isFolder=true,isDefault=false){
                this.newFolderOrPad.name = '';
                this.newFolderOrPad.type = '';
                this.newFolderOrPad.order = '';

                this.defaultOpt.isFolder = isFolder;
                this.defaultOpt.isDefault = isDefault;
            },
            /****************** Index BD **********************/
            initIndexDBCompleted(){
                var that = this;
                PadsService.get().then(function (res) {
                    var data = res.data;
                    if(data && data.folders){
                        
                        EventHub.folders = data.folders;
                        
                        that.folders = EventHub.folders;

                        for (let i = 0; i < EventHub.folders.length; i++) {
                            var item;
                            var queue = [EventHub.folders[i]];
                            while(queue.length){
                                item = queue.shift();
                                if(item.isDefault){
                                    EventHub.defaultFolder = item;
                                    EventHub.defaultFolderId = item.id; 
                                    break;
                                }
                                if(item.children && item.children.length){
                                    item.children.forEach(son=> queue.push(son));
                                }
                            }
                        }
                        EventHub.$emit("initFolderPropCompleted");
                    }
                }).catch(function (err) {
                    EventHub.$emit('goTip',["获取目录失败!",false]);
                    console.log(err);
                });
            },

            /*** Persistence Data ***/
            putPad(newPad,msg,callback){
                PadsService.put(newPad).then(function () {
                    if(msg.isTip)
                        EventHub.$emit('goTip', [msg.successful]);
                    if(callback)
                        callback();
                }).catch(function (err) {
                    EventHub.$emit('goTip', [msg.unsuccessful,false]);
                    console.log(err);
                });
            },
            putPadToSepciPlace(padBox,msg,callback){
                PadsService.putPadToSepciPlace({
                    pad:padBox.pad,
                    orderId:padBox.orderId
                }).then(function (res) {
                    if(msg.isTip)
                        EventHub.$emit('goTip',[msg.successful]);
                    if(callback)
                        callback();
                }).catch(function (err) {
                    EventHub.$emit('goTip',[msg.unsuccessful,false,3000]);
                    console.error(err);
                });
            }
            /*** Persistence Data ***/
        },
        watch: {

        },
    }
</script>

