package red.medusa.miniblog.pad.vo;

import red.medusa.miniblog.pad.bean.Pad;

public class PadBox {
    private Pad pad;
    private String orderId;

    public Pad getPad() {
        return pad;
    }

    public void setPad(Pad pad) {
        this.pad = pad;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


}
