import Vue from "vue"

const PADS_REST = "/miniblog/miniblog/data/pad";
const GET_FOLDER = "/miniblog/padRoot";

function Pads() {

    this.get = function () {
        return Vue.http.get(GET_FOLDER);
    };

    this.put = function (pad) {
        return Vue.http.put(PADS_REST + "/" + pad.id, pad);
    };
    this.delete = function (id) {
        return Vue.http.delete(PADS_REST + "/" + id);
    };

}

export default Pads;