package controller.ftp_controller;

import auth.FtpClientConnection;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/ftp_rename")
public class FtpClientRenameController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionLogin = req.getSession(false);
        int connectionCount = 0;

        while (connectionCount < 20) {
            try {
                connectionCount++;
                FTPClient client = FtpClientConnection.getFtpClientConnection(sessionLogin,connectionCount);
                if (client.isConnected()) {
                    if (client.rename(req.getParameter("oldName"), req.getParameter("newName"))) {
                        resp.getWriter().println(true);
                        connectionCount = 20;
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}
