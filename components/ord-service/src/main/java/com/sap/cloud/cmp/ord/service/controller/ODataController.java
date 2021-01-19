package com.sap.cloud.cmp.ord.service.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.olingo.jpa.processor.core.api.JPAClaimsPair;
import com.sap.olingo.jpa.processor.core.api.JPAODataCRUDContextAccess;
import com.sap.olingo.jpa.processor.core.api.JPAODataClaimsProvider;
import com.sap.olingo.jpa.processor.core.api.JPAODataGetHandler;

import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/${odata.jpa.request_mapping_path}/**")
public class ODataController {

    @Autowired
    private JPAODataCRUDContextAccess serviceContext;

    @RequestMapping(value = "**", method = { RequestMethod.GET })
    public void handleODataRequest(HttpServletRequest request, HttpServletResponse response) throws ODataException {
        final JPAODataGetHandler handler = new JPAODataGetHandler(serviceContext);
        //handler.getJPAODataRequestContext().setDebugSupport(new DefaultDebugSupport()); // Use query parameter odata-debug=json to activate.
        // Activate debug support after security is in place
        handler.getJPAODataRequestContext().setClaimsProvider(createClaims(request));
        handler.process(request, response);
    }

    private JPAODataClaimsProvider createClaims(final HttpServletRequest req) {
        final String auth = req.getHeader("tenant");
        System.out.println(">>>>>>>>>" + auth);
        final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();
        if (auth != null && !auth.isEmpty()) {
          System.out.println(">>>>>>> " + auth);
          final JPAClaimsPair<String> user = new JPAClaimsPair<>(auth);
          claims.add("tenant_id", user);
        }
        return claims;
      }
}
