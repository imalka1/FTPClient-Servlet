package auth;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class FtpClientConnection {
    private static FtpClientConnection ftpClientConnection;
    private FTPClient ftpClient;

    private FtpClientConnection(String username, String password) throws IOException {
//        try {
        InputStream input = new FileInputStream("C:\\FtpClientProperties\\server.properties");
        Properties prop = new Properties();

        prop.load(input);

        ftpClient = new FTPClient();
        ftpClient.connect(prop.getProperty("server"), 21);

//        int reply = ftpClient.getReplyCode();

//        !FTPReply.isPositiveCompletion(reply)
        if (!ftpClient.login(username, password)) {
            ftpClient.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftpClient.enterLocalPassiveMode();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static FTPClient getFtpClientConnection(String username, String password, HttpSession sessionLogin) throws IOException {
        return createFtpClient(username, password, sessionLogin);
    }

    public static FTPClient getFtpClientConnection(HttpSession sessionLogin) throws IOException {
        return createFtpClient(sessionLogin.getAttribute("username").toString(), sessionLogin.getAttribute("password").toString(), sessionLogin);
    }

    private static FTPClient createFtpClient(String username, String password, HttpSession sessionLogin) throws IOException {
        if (sessionLogin.getAttribute("ftpClientobj") == null) {
            ftpClientConnection = new FtpClientConnection(username, password);
            sessionLogin.setAttribute("ftpClientobj", ftpClientConnection.getFtpClient());
            sessionLogin.setAttribute("username", username);
            sessionLogin.setAttribute("password", password);
        } else {
            try {
                ftpClientConnection.getFtpClient().sendNoOp();
            } catch (IOException e) {
                ftpClientConnection = new FtpClientConnection(username, password);
                sessionLogin.setAttribute("ftpClientobj", ftpClientConnection.getFtpClient());
            }
        }
        return (FTPClient) sessionLogin.getAttribute("ftpClientobj");
    }

    private FTPClient getFtpClient() {
        return ftpClient;
    }
}
