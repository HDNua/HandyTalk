package com.tistory.hdnua.sutda;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Sutda Server입니다.
 */
public class Server {
    //region 필드를 정의합니다.
    ClientDataBase logOnClients;
    ClientDataBase registeredClients;

    ArrayList<ClientInfo> logOnClientQueue = new ArrayList<>();

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 서버를 시작합니다.
     */
    public void go() {
        try {
            // 필드를 초기화합니다.
            initFields();

            // 서버 소켓을 생성합니다.
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server address is " + serverSocket.getInetAddress().getHostAddress());

            // 서버 루프입니다.
            while (true) {
                try {
                    // 클라이언트 소켓의 연결을 대기합니다.
                    System.out.println("Server is listening...");
                    Socket clientSocket = serverSocket.accept();

                    // 클라이언트 핸들러 Job을 생성합니다.
                    Job job = getJob(this, clientSocket);

                    // 클라이언트와의 통신이 시작되었습니다.
                    // 새 클라이언트의 연결을 대기하기 위해 루프의 처음으로 올라갑니다.
                    if (job instanceof RegisterJob) {
                        // 스레드를 생성합니다.
                        Thread thread = new Thread(job);
                        thread.start();

                        System.out.println("RegisterJob created");
                        thread.join();
                        RegisterJob registerJob = (RegisterJob)job;
                        if (registerJob.getErrorMessage() != null)
                            throw new HDException(registerJob.getErrorMessage(), null);
                        System.out.println("from user: " + registerJob.getClientInfo().getUserName());

                        // 클라이언트를 사용자 리스트에 추가합니다.
                        registerClient(job.getClientInfo());
                        System.out.println("새 사용자 등록: " + job.getClientInfo().getUserName());
                    }
                    else if (job instanceof LoginJob) {
                        // 스레드를 생성합니다.
                        Thread thread = new Thread(job);
                        thread.start();

                        System.out.println("LoginJob created");
                        thread.join();
                        LoginJob loginJob = (LoginJob)job;
                        if (loginJob.getErrorMessage() != null)
                            throw new HDException(loginJob.getErrorMessage(), null);
                        System.out.println("from user: " + loginJob.getClientInfo().getUserName());

                        // 클라이언트를 로그인 사용자 리스트에 추가합니다.
                        ClientInfo clientInfo = job.getClientInfo();
                        acceptClientLogin(clientInfo);
                        enqueueClientInfo(clientInfo);
                        System.out.println("사용자 접속: " + clientInfo.getUserName());
                    }
                    else if (job instanceof WaitingRoomJob) {
                        WaitingRoomJob waitingRoomJob = (WaitingRoomJob)job;
                        ClientInfo clientInfo = dequeueClientInfo();
                        ClientInfo jobClientInfo = job.getClientInfo();
                        for (ClientInfo client: logOnClients) {
                            if (client.getUserName().equals(clientInfo.getUserName())) {
                                client.setProperties(job.socket, job.reader, job.writer);
                                break;
                            }
                        }
                        waitingRoomJob.setClientInfo(clientInfo);

                        // 스레드를 생성합니다.
                        Thread thread = new Thread(waitingRoomJob);
                        thread.start();

                        //
                        System.out.println("WaitingRoomJob created");

                    }
                    /*
                    else if (job instanceof GameJob) {
                        //

                    }
                    */
                    else {
                        throw new NotImplementedException();
                    }
                }
                catch (HDException ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 필드를 초기화합니다.
     */
    private void initFields() throws IOException {
        logOnClients = new ClientDataBase();

        registeredClients = new ClientDataBase();
        initRegisteredClients();
    }
    /**
     * 등록 클라이언트 목록을 초기화합니다.
     * @throws IOException 파일 입력이 실패하면 발생합니다.
     */
    private void initRegisteredClients() throws IOException {
        File file = new File(Handy.CLIENT_DATABASE_FILE);
        if (file.exists() == false) {
            file.createNewFile();
        }

        FileInputStream fin = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fin);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty())
                continue;
            registerClient(new ClientInfo(line, null, null, null));
        }
        reader.close();

        printRegisteredClients();
        System.out.println("사용자 등록 완료");
    }
    /**
     * 수신 메시지에 따라 Thread에서 실행할 Job을 결정합니다.
     * @return Thread가 실행할 수 있는 Runnable 객체를 반환합니다.
     * @param server 서버 객체입니다.
     * @param socket 연결된 클라이언트 소켓입니다.
     */
    private Job getJob(Server server, Socket socket) throws IOException, HDException {
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        // 수신 메시지에 따라 Thread에서 실행할 Job을 결정합니다.
        Job ret;
        String message = recv(reader);
        switch (message) {
            // 'RegisterForm'으로부터 메시지를 수신했습니다.
            case Handy.COMMAND_REGISTER:
                ret = new RegisterJob(server, socket, reader, writer);
                break;

            // 'LoginForm'으로부터 메시지를 수신했습니다.
            case Handy.COMMAND_LOGIN:
                ret = new LoginJob(server, socket, reader, writer);
                break;

            // 'WaitingForm'으로부터 메시지를 수신했습니다.
            case Handy.COMMAND_WAITING:
                ret = new WaitingRoomJob(server, socket, reader, writer);
                break;

            // 'GameForm'으로부터 메시지를 수신했습니다.
            case Handy.COMMAND_GAME:
                ret = new GameJob(server, socket, reader, writer);
                break;

            // 잘못된 메시지를 수신했습니다.
            default:
                throw new HDException("Job을 획득할 수 없습니다.", null);
        }
        return ret;
    }

    //endregion



    //region 메서드를 정의합니다.
    private void registerClient(ClientInfo client) {
        registeredClients.add(client);
        updateClientDatabaseFile();
        printRegisteredClients();
    }
    private void removeRegisteredClient(ClientInfo client) {
        registeredClients.remove(client);
        updateClientDatabaseFile();
        printRegisteredClients();
    }
    private void acceptClientLogin(ClientInfo client) {
        logOnClients.add(client);
        printLogOnClients();
    }
    private void acceptClientLogout(ClientInfo client) {
        logOnClients.remove(client);
        printLogOnClients();
    }
    public void requestRegisterClient(ClientInfo client) {
        registerClient(client);
    }
    public void requestRemoveClient(ClientInfo client) {
        removeRegisteredClient(client);
    }
    public void requestLoginClient(ClientInfo client) {
        acceptClientLogin(client);
    }
    public void requestLogoutClient(ClientInfo client) {
        acceptClientLogout(client);
    }


    /**
     * 소켓 writer로 문자열을 출력합니다.
     * @param writer 메시지 송신 스트림입니다.
     * @param message 전송할 메시지입니다.
     */
    private void send(PrintWriter writer, String message) {
        writer.println(message);
        writer.flush();
    }
    /**
     * 소켓 reader로부터 문자열을 획득합니다.
     * @param reader 메시지 수신 스트림입니다.
     * @throws IOException readLine() 메서드가 실패하면 예외를 던집니다.
     */
    private String recv(BufferedReader reader) throws IOException {
        String message;
        while ((message = reader.readLine()) != null)
            break;
        return message;
    }


    /**
     * 클라이언트 이름이 데이터베이스에 존재하는지 확인합니다.
     * @return 존재한다면 참입니다.
     */
    public boolean doesClientNameExist(String clientName) {
        return registeredClients.exist(new ClientInfo(clientName, null, null, null));
    }
    /**
     * 클라이언트가 로그인 상태인지 확인합니다.
     * @return 로그인 상태라면 참입니다.
     */
    public boolean doesClientLogOn(String userID) {
        return logOnClients.exist(new ClientInfo(userID, null, null, null));
    }


    /**
     *
     * @param info
     */
    public void requestTell(MessageInfo info) {
        if (info.getTargetName() == null) {
            String message = String.format("%s: %s", info.getSenderName(), info.getMessage());
            System.out.println("Begin to send message -> " + message);

            for (ClientInfo client : logOnClients) {
                PrintWriter writer = client.getWriter();
                writer.println(message);
                writer.flush();
            }
            System.out.println("End of message passing");
        }
        else {

        }
    }


    /**
     *
     */
    public void updateClientDatabaseFile() {
        // 파일에 정보를 출력합니다.
        try {
            FileWriter fwriter = new FileWriter(Handy.CLIENT_DATABASE_FILE);
            PrintWriter writer = new PrintWriter(fwriter);
            for (ClientInfo client: registeredClients) {
                writer.println(client.getUserName());
            }
            writer.close();
            System.out.println("클라이언트 리스트 업데이트 완료");
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }

    /**
     * 등록된 사용자 리스트를 출력합니다.
     */
    private void printRegisteredClients() {
        System.out.print("등록된 사용자 리스트: ");
        registeredClients.print();
    }
    /**
     * 로그인한 사용자 리스트를 출력합니다.
     */
    private void printLogOnClients() {
        System.out.print("로그인한 사용자 리스트: ");
        logOnClients.print();
    }

    /**
     * 로그인 중인 클라이언트 정보를 로그온 큐에 넣습니다.
     */
    private synchronized void enqueueClientInfo(ClientInfo client) {
        logOnClientQueue.add(client);
    }
    /**
     * 로그인 중인 클라이언트 정보를 가져옵니다.
     */
    private synchronized ClientInfo dequeueClientInfo() {
        return logOnClientQueue.remove(0);
    }

    //endregion





    //region 구형 정의를 보관합니다.


    //endregion
}
