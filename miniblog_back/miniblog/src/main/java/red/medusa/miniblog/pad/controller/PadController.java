package red.medusa.miniblog.pad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import red.medusa.miniblog.pad.bean.Pad;
import red.medusa.miniblog.pad.service.PadService;
import red.medusa.miniblog.pad.vo.PadBox;
import red.medusa.miniblog.pad.vo.PadRoot;

import java.util.List;

@RestController
public class PadController {

    @Autowired
    private PadService padService;

    @GetMapping("/padRoot")
    public PadRoot getPadFolder(){
        return padService.getFolderFromPads();
    }

    @PostMapping(value = "/deleteCardAndPadByIds")
    public ResponseEntity deleteCardAndPadByIds(@RequestBody String[] ids){
        return padService.deleteCardAndPadByIds(ids);
    }

    @PostMapping(value = "/moveToOther")
    public ResponseEntity moveToOther(@RequestBody List<Pad> pads){
        return padService.moveToOther(pads);
    }

    @PostMapping(value = "/updatePad")
    public ResponseEntity updatePad(@RequestBody List<Pad> pads){
        return padService.updatePad(pads);
    }

    @PostMapping(value = "/addPadToSepciPlace")
    public ResponseEntity addPadToSepciPlace(@RequestBody PadBox padBox){
        return padService.addPadToSepciPlace(padBox);
    }
}
