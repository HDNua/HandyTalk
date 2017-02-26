package com.tistory.hdnua.sutda;

/**
 * 메시지 분석 정보입니다.
 */
public class MessageInfo {
    private String senderName;
    private String targetName;
    private String message;
    public String getSenderName() { return senderName; }
    public String getTargetName() { return targetName; }
    public String getMessage() { return message; }

    /**
     * 메시지 분석 정보입니다.
     * @param senderName 메시지를 보낸 클라이언트의 이름입니다.
     * @param targetName 메시지를 수신할 클라이언트의 이름입니다. 전체 메시지라면 null입니다.
     * @param message 보낼 메시지입니다.
     */
    public MessageInfo(String senderName, String targetName, String message) {
        this.senderName = senderName;
        this.targetName = targetName;
        this.message = message;
    }

    /**
     * 메시지를 분석합니다.
     * @param recvMessage 분석할 메시지입니다.
     * @return 메시지 분석 결과를 MessageInfo 객체로 반환합니다.
     * @throws HDException 분석할 수 없는 메시지면 예외를 던집니다.
     */
    public static MessageInfo interpret(String recvMessage) throws HDException {
        // 정규식을 이용하여 토큰을 분리합니다.
        String[] word = recvMessage.split("[: ]");
        String senderName = word[0];
        String targetName = null;
        String message;

        // 첫 번째 토큰이 명령인지에 따라 분기합니다.
        if (word.length < 2) {
            return null;
        }

        String token = word[1];
        if (token.equals("/w")) { // 비밀 메시지를 보냅니다.
            targetName = word[2]; // 형식: /w <TARGET_CLIENT_NAME> <MESSAGE>

            // 보낼 메시지를 분리하여 획득합니다.
            String msg1 = recvMessage.substring(senderName.length() + 1);
            String msg2 = msg1.substring(msg1.indexOf("/w") + 3);
            String msg3 = msg2.substring(msg2.indexOf(targetName) + targetName.length() + 1);
            message = msg3;
        }
        else {
            // 모든 클라이언트게 메시지를 보낼 때는 targetName에 null을 넣습니다.
            targetName = null;

            // 클라이언트가 보낸 문자열의 형식은 [<SENDER_NAME>:<MESSAGE>]입니다.
            // 따라서 콜론 이후의 문자열만이 메시지입니다.
            message = recvMessage.substring(senderName.length() + 1);
        }

        // 메시지 객체를 생성하고 정보를 채운 후 반환합니다.
        MessageInfo ret = new MessageInfo(senderName, targetName, message);
        return ret;
    }
}
