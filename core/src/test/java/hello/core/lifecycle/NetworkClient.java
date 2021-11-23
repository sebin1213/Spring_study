// 애노테이션 @PostConstruct, @PreDestroy
package hello.core.lifecycle;
import javax.annotation.PostConstruct; // javax 자바에서 공식으로 지원하는거
import javax.annotation.PreDestroy;

public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }
    public void setUrl(String url) {
        this.url = url;
    }
    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }
    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }
    //서비스 종료시 호출
    public void disConnect() {
        System.out.println("close + " + url);
    }
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}







//빈 등록 초기화, 소멸 메서드 지정///////////////////////////////////////////////////////////////////////
//package hello.core.lifecycle;
//
//public class NetworkClient {
//    private String url;
//    public NetworkClient() {
//        System.out.println("생성자 호출, url = " + url);
//    }
//    public void setUrl(String url) {
//        this.url = url;
//    }
//    //서비스 시작시 호출
//    public void connect() {
//        System.out.println("connect: " + url);
//    }
//    public void call(String message) {
//        System.out.println("call: " + url + " message = " + message);
//    }
//    //서비스 종료시 호출
//    public void disConnect() {
//        System.out.println("close + " + url);
//    }
//    public void init() {
//        System.out.println("NetworkClient.init");
//        connect();
//        call("초기화 연결 메시지");
//    }
//    public void close() {
//        System.out.println("NetworkClient.close");
//        disConnect();
//    }
//}




//인터페이스/////////////////////////////////////////////////////////////////////
//package hello.core.lifecycle;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//
//public class NetworkClient implements InitializingBean, DisposableBean {
//    private String url;
//
//    public NetworkClient() {
//        System.out.println("생성자 호출, url = " + url);
//    }
//    public void setUrl(String url) {
//        this.url = url;
//    }
//    //서비스 시작시 호출
//    public void connect() {
//        System.out.println("connect: " + url);
//    }
//    public void call(String message) {
//        System.out.println("call: " + url + " message = " + message);
//    }
//    //서비스 종료시 호출
//    public void disConnect() {
//        System.out.println("close + " + url);
//    }
//    @Override
//    public void afterPropertiesSet() throws Exception { // 의존관계 주입이 끝난다음에 호출
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메시지");
//    }
//    @Override // 싱글톤 빈이 다~~ 죽으면 호출
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        disConnect();
//    }
//}




///////////////////////////////////////////////////////////////////////////////////////////////////
//
//package hello.core.lifecycle;
//
//public class NetworkClient {
//
//    private String url;
//
//    public NetworkClient() {
//        System.out.println("생성자 호출, url = " + url);
//        connect(); // 객체를 생성할때  connect(연결)하고 연결메세지 호출
//        call("초기화 연결 메시지");
//    }
//
//    // setter 주입 (자동 주입아니라 수동 주입)
//    public void setUrl(String url) { //외부에서 setter로 url을 받아옴
//        this.url = url;
//    }
//
//    //서비스 시작시 호출
//    public void connect() { // 얘는 그냥 실제 네트워크에 붙지는 않을꺼지만 그래도 일단 해보자
//        System.out.println("connect = " + url);
//    }
//
//    public void call(String message) { // 연결이 된상태에서 call을 부를수 있음
//        System.out.println("call: " + url + " message = " + message);
//    }
//
//    //서비스 종료시 호출
//    public void disconnect() {
//        System.out.println("close: " + url);
//    }
//}