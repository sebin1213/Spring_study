package hello.advanced.trace;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TraceId {
    private String id;
    // level을 깊이를 뜻한다.
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    public TraceId(String id, int level){
        this.id = id;
        this.level = level;
    }

    private String createId(){
        // 가끔 중복나지만 로그니까..!ㅎ
        return UUID.randomUUID().toString().substring(0,8);
    }

    public TraceId createNextId(){
        return new TraceId(id, level +1);
    }

    public TraceId createPreviousId(){
        return new TraceId(id,level -1);
    }
}
