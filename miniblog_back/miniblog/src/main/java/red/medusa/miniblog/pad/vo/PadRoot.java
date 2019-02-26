package red.medusa.miniblog.pad.vo;

import red.medusa.miniblog.pad.bean.Pad;

import java.util.ArrayList;
import java.util.List;

public class PadRoot{

    private String id;
    private List<Pad> folders = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Pad> getFolders() {
        return folders;
    }

    public void setFolders(List<Pad> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return "PadRoot{" +
                "id='" + id + '\'' +
                ", folders=" + folders +
                '}';
    }
}
