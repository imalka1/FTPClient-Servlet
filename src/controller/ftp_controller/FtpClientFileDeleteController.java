package controller.ftp_controller;

import auth.FtpClientConnection;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/ftp_delete")
public class FtpClientFileDeleteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int connectionCount = 0;

        while (connectionCount < 10) {
            try {
                connectionCount++;
                FTPClient client = FtpClientConnection.getFtpClientConnection().getFtpClient();
                if (client.isConnected()) {
                    if (client.removeDirectory(req.getParameter("folderPath"))) {
                        resp.getWriter().println(true);
                        connectionCount = 10;
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}