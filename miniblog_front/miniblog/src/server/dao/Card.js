import Vue from "vue"

const PADS_REST = "/miniblog/miniblog/data/card";

function Card() {
    this.get = function (id) {
        return Vue.http.get(PADS_REST + "/" + id);
    };

    this.delete = function (id) {
        return Vue.http.delete(PADS_REST + "/" + id);
    };

    this.put = function (pad) {
        return Vue.http.put(PADS_REST+ "/" + pad.id, pad);
    };

}

export default Card;