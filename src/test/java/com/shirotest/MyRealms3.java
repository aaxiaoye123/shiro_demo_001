package com.shirotest;


import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

public class MyRealms3 implements Realm {

    //返回Realm的名字
    public String getName() {
        return "MyRealms3";
    }

    //支持的效验凭证（凭证有很多，用户名密码只是一种凭证，除了用户名密码之外，还有数字证书，它也是一种凭证）
    //这就是表示MyRealms1将来在进行认证的时候，都支持那些格式，那么就在这个supports()里面写
    public boolean supports(AuthenticationToken authenticationToken) {
        //此处只让Realm支持用户名和密码格式
        //instanceof就是看两个类型是否相等
        //如果返回为true，那么就表示它是UsernamePasswordToken，否则就不是
        return authenticationToken instanceof UsernamePasswordToken;
    }

    //获取凭证的信息
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println(3);

        //获取凭证的信息，那么作为用户名-密码而言，首先要获取的是用户名
        //获取凭证的用户名
        String username = (String)authenticationToken.getPrincipal();

        //获取凭证的密码
        //先转换为一个字符数组，然后创建字符串，将这些字符数组再转为字符串
        String password=new String((char[])authenticationToken.getCredentials());

        //验证用户名
        if(!username.equals("wang")){
            //UnknownAccountException：账号未知异常
            throw new UnknownAccountException(); //账号错误
        }

        if(!password.equals("123")){
            //IncorrectCredentialsException：密码错误异常
            throw  new IncorrectCredentialsException();  //密码错误
        }

        //如果上面的情况不满足，那么就说明账号和密码正确，那么此时返回正确的凭证对象
        //这个凭证对象将来输出的就是username,password,getName()
        return new SimpleAuthenticationInfo(username,password,getName());
    }
}
