package com.shirotest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

public class TestShiro {

    @Test
    public void testShiro1(){
        //1、获取工厂
        //用来获取SecurityManager工厂的，之前说过，SecurityManager是用来管理所有的
        //IniSecurityManagerFactory()连要给上一个类路径，就是shiro1.ini的位置
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro1.ini");


        //2、创建工厂实例
        SecurityManager instance = factory.getInstance();

        //3、将instance注册一下（相当于绑定）
        SecurityUtils.setSecurityManager(instance);


        ///4、获取subject以及用户名密码认证
        //Subject是用来做认证的
        Subject subject = SecurityUtils.getSubject();

        //5、用户名密码认证
        //UsernamePasswordToken()方法里面的是之前设置的用户名和密码，有了这个就可以进行认证
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("zhang","123");

        //6、调用方法进行认证
        try {
            //这段代码可以不写在try...catch，也可以写在try...catch里面
            subject.login(usernamePasswordToken);
        }catch (AuthenticationException e){
            //AuthenticationException：表示认证时异常
            //身份认证失败（如果放在控制器里面，就应该向异常页面跳转）
            System.out.println("认证失败");
        }

        //7、认证通过，结果进行比较
        //subject.isAuthenticated()返回结果与true进行比较
        Assert.assertEquals(true,subject.isAuthenticated());

        //8、认证的退出
        //一定要进行认证的退出，不然会在缓存里面存储着
        subject.logout();


        /*
        * 此时参看结果，显示认证通过，按摩这个意思就是在ini文件里面春粗了一个“zhang”，“123”，
        * 那么用Shiro进行验证的，“zhang”的密码也是“123”，那么验证的结果是通过的
        * 相当于用户输入了一个用户名“zhang”，密码“123”，而数据库里面存储了一个“zhang”“123”
        * 那么这里面那个匹配结果就是true，那么与前面期望的结果true相同，那么就表示验证通过
        *
        * */
    }

    @Test
    public void testShiro2(){
        //获取工厂
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro2.ini");

        //创建工厂实例

        SecurityManager instance = factory.getInstance();

        SecurityUtils.setSecurityManager(instance);
        //获取subject 以及用户名密码认证

        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhang","123");

        //调用方法认证
        try{
            subject.login(usernamePasswordToken);
        }catch(AuthenticationException e){
            //身份认证失败
            System.out.println("认证失败...");
        }

        Assert.assertEquals(true,subject.isAuthenticated());

        //退出
        subject.logout();
    }

    @Test
    public void customMutiRealms(){
        //获取工厂
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro3.ini");

        //创建工厂实例

        SecurityManager instance = factory.getInstance();

        SecurityUtils.setSecurityManager(instance);
        //获取subject 以及用户名密码认证

        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhang","123");

        //调用方法认证
        try{
            subject.login(usernamePasswordToken);
        }catch(AuthenticationException e){
            //身份认证失败
            System.out.println("认证失败...");
        }

        Assert.assertEquals(true,subject.isAuthenticated());

        //这个的意思是想表示Realm的通过有两个，myRealm1和myRealm2都校验的是"zhang""123"，myRealm3娇艳的是“王”“123”，这个是不用通过的
        //输出结果：
        //Expected :2
        //Actual   :1
        //这个输出结果表示：我说这个Realm的通过有2个，但是它返回的说是1个，那么就说明了这个方法
        /*getPrincipals()：获得的是认证通过的身份信息，虽然有两个Realm（myRealm1和myRealm2），但是校验的信息都是“zhang”“123”，
        实际上它属于一个账户，所以这里的大小获取的是1，虽然两个账户里面都是“zhang”“123”，但是它获得的是1
         */
//        Assert.assertEquals(2,subject.getPrincipals().asList().size());
        //此时将这个改为Realm的通过有一个，那么结果就正确了，可以通过
        Assert.assertEquals(1,subject.getPrincipals().asList().size());
        //退出
        subject.logout();

    }
    @Test
    public void testJdbcReam(){
        //获取工厂
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:jdbc.ini");

        //创建工厂实例

        SecurityManager instance = factory.getInstance();

        SecurityUtils.setSecurityManager(instance);
        //获取subject 以及用户名密码认证

        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhang","123");

        //调用方法认证
        try{
            subject.login(usernamePasswordToken);
        }catch(AuthenticationException e){
            //身份认证失败
            System.out.println("认证失败...");
        }
        Assert.assertEquals(true,subject.isAuthenticated());

        //退出
        subject.logout();

    }

    //校验的时候，每次到要重复的写上面的获取工程、创建工厂等等的步骤，那么就可以抽象成一个方法
    public void login(String configFile){
        //获取工厂
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory(configFile);

        //创建工厂实例

        SecurityManager instance = factory.getInstance();

        SecurityUtils.setSecurityManager(instance);
        //获取subject 以及用户名密码认证

        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhang","123");

        //调用方法认证
        try{
            subject.login(usernamePasswordToken);
        }catch(AuthenticationException e){
            //身份认证失败
            System.out.println("认证失败...");
        }
        Assert.assertEquals(true,subject.isAuthenticated());

        //退出
        subject.logout();
    }


    @Test
    public void testStrategy(){
        login("classpath:shiro4.ini");


    }



}

