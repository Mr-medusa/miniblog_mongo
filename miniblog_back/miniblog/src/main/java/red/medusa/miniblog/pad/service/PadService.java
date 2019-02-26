package red.medusa.miniblog.pad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import red.medusa.miniblog.pad.bean.Card;
import red.medusa.miniblog.pad.bean.Pad;
import red.medusa.miniblog.pad.vo.PadBox;
import red.medusa.miniblog.pad.vo.PadRoot;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Service
public class PadService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 构建目录树
     */
    public PadRoot getFolderFromPads() {

        PadRoot folder = new PadRoot();

        List<Pad> pads = mongoTemplate.findAll(Pad.class);

        if (pads.isEmpty()) {
            return folder;
        }

        Map<String, Pad> findPadParentNodeMap = new HashMap<>();

        //构建一个Hash表
        pads.forEach(pad -> findPadParentNodeMap.put(pad.getId(), pad));

        //构建目录树
        pads.forEach(pad -> {

            String pid = pad.getParentId();

            if (pid != null) {

                Pad padParent = findPadParentNodeMap.get(pid);

                if (pid.equals(padParent.getId())) {

                    if (padParent.getChildren() == null) {
                        padParent.setChildren(new ArrayList<>());
                    }

                    padParent.getChildren().add(pad);
                }

            } else {
                folder.getFolders().add(pad);
            }
        });

        //排序
        folder.getFolders().forEach(root -> {

            Pad p;

            LinkedList<Pad> queue = new LinkedList(Arrays.asList(root));

            while (!queue.isEmpty()) {
                p = queue.pop();

                if (p.getChildren() != null && !p.getChildren().isEmpty()) {

                    p.getChildren().sort(Comparator.comparing(e -> e.getOrder()));

                    p.getChildren().forEach(child -> {

                        if (child.getChildren() != null && !child.getChildren().isEmpty()) {
                            queue.push(child);
                        }

                    });
                }
            }
        });

        folder.getFolders().sort(Comparator.comparing(e -> e.getOrder()));

        return folder;
    }


    public ResponseEntity deleteCardAndPadByIds(String ids[]) {

        mongoTemplate.remove(query(where("id").in(ids)), Pad.class);

        mongoTemplate.remove(query(where("id").in(ids)), Card.class);

        return ResponseEntity.ok("删除成功!");
    }

    public ResponseEntity moveToOther(List<Pad> pads) {

        pads.forEach(pad -> mongoTemplate.findAndModify(query(where("id").is(pad.getId())), update("parentId", pad.getParentId()), Pad.class));

        return ResponseEntity.ok("移动成功!");
    }

    public ResponseEntity updatePad(List<Pad> pads) {

        pads.forEach(pad -> mongoTemplate.findAndModify(query(where("id").is(pad.getId())),

                update("name", pad.getName())
                        .set("type", pad.getType())
                        .set("order", pad.getOrder())
                        .set("isDefault", pad.getIsDefault()),
                Pad.class));

        return ResponseEntity.ok("移动成功!");
    }

    public ResponseEntity addPadToSepciPlace(PadBox padBox) {

        if (padBox.getPad() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("操作失败!");

        Pad pad = padBox.getPad();
        String orderId = padBox.getOrderId();
        String parentId = padBox.getPad().getParentId();

        List<Pad> children = mongoTemplate.find(query(where("parentId").is(parentId)), Pad.class);

        children.sort(Comparator.comparing(e -> e.getOrder()));

        boolean find = false;

        Integer order = null;

        for (Pad child : children) {

            if (child.getId().equals(orderId)) {

                order = children.indexOf(child);

                pad.setOrder(order);

                mongoTemplate.save(pad);

                find = true;
            }

            if (find) {
                //从新设置序号
                mongoTemplate.findAndModify(query(where("id").is(child.getId())), update("order", ++order), Pad.class);
            }
        }

        return ResponseEntity.ok("操作成功!");
    }
}
