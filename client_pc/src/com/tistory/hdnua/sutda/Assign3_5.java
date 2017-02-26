package com.tistory.hdnua.sutda;

public class Assign3_5 {
    public static void main(String[] args) {
        // 학번과 이름을 출력합니다.
        System.out.println("2011722086 한도영");

        // 과제를 해결합니다.
        try {
            int loginFormWidth = 300, loginFormHeight = 250;
            int loginX1 = 100, loginY1 = 100;
            int loginX2 = 400, loginY2 = 300;

            int clientFormWidth = 400, clientFormHeight = 350;
            int clientX1 = 700, clientY1 = 250;
            int clientX2 = 1100, clientY2 = 550;

            String ip = Handy.getServerIP();


            {
                LoginForm loginForm = new LoginForm
                        (loginFormWidth, loginFormHeight, clientFormWidth, clientFormHeight);
                loginForm.setUserNameText("PCUser");
                loginForm.setIPAddressText(ip);
                loginForm.setLocation(loginX1, loginY1);
                loginForm.setClientFormLocation(clientX1, clientY1);
                loginForm.show();
            }

            if (false)
            {
                LoginForm loginForm = new LoginForm
                        (loginFormWidth, loginFormHeight, clientFormWidth, clientFormHeight);
                loginForm.setUserNameText("Anonymous2");
                loginForm.setIPAddressText(ip);
                loginForm.setLocation(loginX2, loginY1);
                loginForm.setClientFormLocation(clientX2, clientY1);
                loginForm.show();
            }

            if (false)
            {
                LoginForm loginForm = new LoginForm
                        (loginFormWidth, loginFormHeight, clientFormWidth, clientFormHeight);
                loginForm.setUserNameText("Anonymous3");
                loginForm.setIPAddressText(ip);
                loginForm.setLocation(loginX1, loginY2);
                loginForm.setClientFormLocation(clientX1, clientY2);
                loginForm.show();
            }

            if (false)
            {
                LoginForm loginForm = new LoginForm
                        (loginFormWidth, loginFormHeight, clientFormWidth, clientFormHeight);
                loginForm.setUserNameText("Anonymous4");
                loginForm.setIPAddressText(ip);
                loginForm.setLocation(loginX2, loginY2);
                loginForm.setClientFormLocation(clientX2, clientY2);
                loginForm.show();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
