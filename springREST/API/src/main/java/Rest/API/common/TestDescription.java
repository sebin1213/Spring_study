package Rest.API.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// JUNIT 5에서는 DISPLAY 어노테이션을 지원하지만 junit4에는 없어 만드는 어노테이션.... 딱히 기능은 없는둣,,,
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE) // 라이프사이클, 해당어노테이션이 언제까지 살아있을지... 얘는 굳이 컴파일 이후에도 필요하지는 않으니 source까지만.. 컴파일되서 클래스파일이 되면 사라짐
public @interface TestDescription {
    String value(); //항상입력하도록함
}
