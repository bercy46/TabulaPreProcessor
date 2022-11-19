package ca.jpti.SuiviBudget.Externe;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import java.io.IOException;
import java.net.MalformedURLException;

public class WindowsShare {
    public static void main(String[] args) {
//        String user = "jacques:jpti0612";
//        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
//        String path = "smb://172.24.101.14/suivibudget/test.txt";
//        SmbFile sFile = null;
//        try {
//            sFile = new SmbFile(path, auth);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        SmbFileOutputStream sfos = null;
//        try {
//            sfos = new SmbFileOutputStream(sFile);
//        } catch (SmbException e) {
//            throw new RuntimeException(e);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            sfos.write("Test".getBytes());
//            sfos.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        CIFSContext base = SingletonContext.getInstance();
        CIFSContext authed1 = base.withCredentials(new NtlmPasswordAuthentication(base, null,
                System.getenv("windowsuser"), System.getenv("windowspassword")));
        try {
            SmbFile f = new SmbFile("smb://172.24.101.14/suivibudget/test.txt", authed1);
            SmbFileOutputStream sfos = new SmbFileOutputStream(f);
            sfos.write("Test".getBytes());
            sfos.close();
        } catch (SmbException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
