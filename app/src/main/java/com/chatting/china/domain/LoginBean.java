package com.chatting.china.domain;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public class LoginBean {

    /**
     * msg : 登录成功
     * code : 1
     * data : {"birthday":"1999-01-01","sex":"男","imageUrl":"https://www.baidu.com","mobile":"17600463503","sign":"退一步海阔天空","location":"北京丰台区角门西","age":20,"email":"fengzhangwei399@yeah.net","username":"张三"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * birthday : 1999-01-01
         * sex : 男
         * imageUrl : https://www.baidu.com
         * mobile : 17600463503
         * sign : 退一步海阔天空
         * location : 北京丰台区角门西
         * age : 20
         * email : fengzhangwei399@yeah.net
         * username : 张三
         */

        private String birthday;
        private String sex;
        private String imageUrl;
        private String mobile;
        private String sign;
        private String location;
        private int age;
        private String email;
        private String username;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
