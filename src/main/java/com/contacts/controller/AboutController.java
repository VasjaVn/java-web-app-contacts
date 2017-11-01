package com.contacts.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AboutController {

    @RequestMapping(value={"/", "/about"}, method = RequestMethod.GET)
    public String index() {
        return "about/index";
    }

    @RequestMapping(value = "/about/downloadApkFile", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadApkFile(HttpServletResponse response) throws IOException {
        final DefaultResourceLoader loader = new DefaultResourceLoader();
        final Resource resource = loader.getResource("classpath:META-INF/android/clientContacts.apk");
        final byte[] bytesApkFile = IOUtils.toByteArray( resource.getInputStream() );

        response.setHeader("Content-Disposition", "attachment; filename=clientContacts.apk");
        response.getOutputStream().write( bytesApkFile );
    }
}