package red.medusa.miniblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import red.medusa.miniblog.blog.bean.BlogTag;
import red.medusa.miniblog.pad.bean.Pad;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
public class MiniblogApplication {

    @Qualifier("padRepository")
    @Autowired
    private MongoRepository padRepository;


    @Qualifier("blogTagRepository")
    @Autowired
    private MongoRepository blogTagRepository;

    public static void main(String[] args) {
        SpringApplication.run(MiniblogApplication.class, args);
    }

    /**
     * 初始化数据库
     */
    @PostConstruct
    public void init() {
        long defaultPadNum = padRepository.count();
        if (defaultPadNum == 0) {
            Pad defaultPad = new Pad("0f49491c-4509-4bd6-a1d8-84f6de7b4b84", null, "DEFAULT", "JavaScript", true, false, true, new Date(), new Date(),0,true,new ArrayList<>());
            padRepository.save(defaultPad);
        }

        long blogTagNum = blogTagRepository.count();
        if(blogTagNum == 0){
            BlogTag blogTag = new BlogTag("blogTags",null);
            blogTagRepository.save(blogTag);
        }
    }

}

