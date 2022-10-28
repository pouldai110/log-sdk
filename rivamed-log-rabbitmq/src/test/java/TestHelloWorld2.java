import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * @author seeeyou
 */
public class TestHelloWorld2 {
    public static void main(String[] args) throws NotFoundException,
            IOException, CannotCompileException, InstantiationException,
            IllegalAccessException, SecurityException, NoSuchMethodException,
            IllegalArgumentException, InvocationTargetException {
        // 用于取得字节码类，必须在当前的classpath中，使用全称
        ClassPool pool = ClassPool.getDefault();
        /**
         * makeClass() cannot create a new interface; makeInterface() in
         * ClassPool can do. Member methods in an interface can be created with
         * abstractMethod() in CtNewMethod. Note that an interface method is an
         * abstract method.
         */
        CtClass ccClass = pool.makeClass("Point");
        String bodyString = "{System.out.println(\"Call to method \");}";
        //为新创建的类新加一个方法execute，无任何参数
        CtMethod n1 = CtNewMethod.make(CtClass.voidType, "execute", null, null,
                bodyString, ccClass);
        ccClass.addMethod(n1);
        /**
         * 这里无法用new的形式来创建一个对象，因为已经classloader中不能有两个相同的对象,否则会报异常如下：
         *Caused by: java.lang.LinkageError: loader (instance of  sun/misc/Launcher$AppClassLoader):
         *attempted  duplicate class definition for name: "Point"
         **/
        Object oo = ccClass.toClass().newInstance();
        Method mms = oo.getClass().getMethod("execute", null);
        System.out.println("new class name is : " + oo.getClass().getName());
        System.out.println("new class's method is : " + mms.invoke(oo, null));
        System.out.println("---------------------------------------------");
        //这一行代码将class冻结了，下面无法再对类多编辑或者修改，下面的setName会报异常如：
        //Exception in thread "main" java.lang.RuntimeException: Point class is frozen
        ccClass.freeze();
        try {
            ccClass.setName("Point2");
        } catch (Exception e) {
            System.out.println(e);
        }
        //对已经冻结的class解冻之后还可以继续编辑修改
        ccClass.defrost();
        System.out.println("------------- 上面的代码是对的，下面的代码将会无法执行出结果，会报错------------------------");
        //第二个方法
        bodyString = "public int getNumber(Integer num){System.out.println(\"Point2 Call to method \");return 10+num;}";
        CtMethod n2 = CtNewMethod.make(bodyString, ccClass);//直接创建一个方法，带有一个int的参数和返回值
        ccClass.addMethod(n2);
        Class[] params = new Class[1];
        Integer num = new Integer(15);
        params[0] = num.getClass(); //就多了下面这个实例化，但是这样会导致一个错误
        oo = ccClass.toClass().newInstance();
        mms = oo.getClass().getMethod("getNumber", params);
        System.out.println("new class name is : " + oo.getClass().getName());
        System.out.println("new class's method is : " + mms.invoke(oo, 100));
        System.out.println("---------------------------------------------");
    }
}
