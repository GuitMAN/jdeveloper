package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import soapclient.GetCompositeInfo;
import soapclient.GetcompositeinfoClientEp;
import soapclient.ProcessResponse;


public class Operation extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    @SuppressWarnings("compatibility:-3054272907918347977")
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {
        String var0 = "";
        try {
            var0 = request.getParameter("param");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Operation</title></head>");
        out.println("<body>");
        out.println("<p>The servlet has received a GET. This is the reply.</p>");

        String test_str[] =
        { "Connecting to JMX Server at host: ulbs8.neoflex.ru port 8001",
          "Connected successfully to: service:jmx:t3://ulbs8.neoflex.ru:8001/jndi/weblogic.management.mbeanservers.runtime",
          "[('CustomerFinancialTransactionInfoCRMReqAV1', '1.22948', 'crm', 'active', 'on', 'false'), ('GetBankAccountHoldRestrictionListPROFProvAV1', '1.16147', 'pf', 'active', 'on', 'false'), ('MDMC_Adapter_Stub_for_tsc', '1.20954', 'stub3', 'active', 'off', 'false'), ('ChangeCustomerPartyRelation', '1.0', 'ulbsm', 'active', 'on', 'false'), ('ChangeCustomerPartyRelation', '1.1729', 'ulbsm', 'active', 'on', 'false'), ('ChangeCustomerPartyRelation', '1.2012', 'ulbsm', 'active', 'on', 'false'), ('ChangeCustomerPartyRelation', '1.492', 'ulbsm', 'active', 'on', 'false'), ('CloseBankCard', '1.0', 'ulbsm', 'active', 'on', 'false'), ('CloseBankCard', '1.1729', 'ulbsm', 'active', 'on', 'false'), ('CloseBankCard', '1.2012', 'ulbsm', 'active', 'on', 'false'), ('CloseBankCard', '1.492', 'ulbsm', 'active', 'on', 'false')]" };


        out.println("<form action=\"\" method=\"post\">");
        out.println("<button type=\"submit\" name=\"oper\" value=\"1\" style=\"background-color: #006A6A;\">Execute</button></form>");
        out.println("</body></html>");
        out.close();
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
                                                            IOException {
        String var0 = "";
        try {
            var0 = request.getParameter("param");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");

        out.println("<head><title>Operation</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head>");
        out.println("<body>");
        out.println("<p>The servlet has received a POST. This is the reply.</p>");
        Process proc;
      /*  if ("1".equals(request.getParameter("oper"))) {
            try {
                String[] cmd =
                { "/bin/sh", "-c", "cd $MW_HOME//Oracle_SOA1/common/bin; ./wlst.sh listDeployedComposites.py" };
                proc = Runtime.getRuntime().exec(cmd);
                InputStream in = proc.getInputStream();
                ArrayList ar = new ArrayList();
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
                String l;
                while ((l = br.readLine()) != null) {
                    ar.add(l);
                    out.println("<p>" + l + "</p>");
                }
                Runtime.getRuntime().exec("dir");
                out.println("<p style=\"background-color: #00FF6A;>Excelent.</p>");
            } catch (Exception e) {
                out.println(e.getMessage());
            }

            out.println("<form action=\"\" method=\"post\">");
            out.println("<p><button type=\"submit\" name=\"oper\" value=\"1\" style=\"background-color: #006A6A;\">Execute</button></form>");
}
*/

        GetcompositeinfoClientEp service;
        service = new GetcompositeinfoClientEp();

        GetCompositeInfo req;
        req = service.getGetCompositeInfoPt();
              
        List<ProcessResponse.Composite> res;
        res = req.process("");

        for(ProcessResponse.Composite item : res){
                out.println(item.getName()+ " | "+ item.getRevision());
        }
        out.println("</body></html>");
        out.close();
    }
}
