package com.company.go;

import com.company.go.application.port.in.global.IndexUseCase;
import com.itextpdf.text.DocumentException;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public class ControllerUtilities {
    static public void storeIndexDetailsInModel(Model model, IndexUseCase index) throws IOException, SQLException {
        model.addAttribute("pictureData", index.getProfilePictureData());
        model.addAttribute("pictureType", index.getProfilePictureType());
        model.addAttribute("alias", index.getAlias());
        model.addAttribute("fullName", index.getFullName());
        model.addAttribute("roles", index.getRoles());
    }

    public static byte[] getReportDownload(Map<String, Object> variables, String templatePath) throws IOException, DocumentException {
        String html = getTemplateContent(variables, templatePath);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(byteStream);
        byte[] bytes = byteStream.toByteArray();
        byteStream.flush();
        byteStream.close();
        return bytes;
    }

    public static void downloadBytes(final byte[] content, HttpServletResponse response, String name) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", StringUtils.isEmpty(name) ? "file.pdf" : name));
        response.setContentLength(content.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        OutputStream outputStream = response.getOutputStream();
        FileCopyUtils.copy(inputStream, outputStream);
        inputStream.close();
    }

    private static String getTemplateContent(Map<String, Object> variables, String templatePath){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setCacheable(false);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        context.setVariables(variables);
        context.setVariable("baseUrl", getCurrentBaseUrl());
        return templateEngine.process(templatePath, context);
    }

    private static String getCurrentBaseUrl() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }

}
